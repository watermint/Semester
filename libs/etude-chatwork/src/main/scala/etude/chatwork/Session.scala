package etude.chatwork

import etude.http._
import java.net.URI
import java.time.{Duration, Instant}
import scala.util.parsing.json.{JSON, JSONArray, JSONObject}
import scala.collection.mutable
import org.slf4j.LoggerFactory
import etude.qos.Throttle

/**
 *
 */
case class Session(email: String,
                   password: String,
                   orgId: Option[String]) {
  /**
   * Wait login action for seconds.
   */
  lazy val LOGIN_ACTION_SPAN = 10

  lazy val throttle: Throttle = Throttle(0.5)

  lazy val isKddiChatwork: Boolean = {
    orgId match {
      case Some(o) => true
      case _ => false
    }
  }

  lazy val baseUri: URI = {
    if (isKddiChatwork) {
      new URI("https://kcw.kddi.ne.jp/")
    } else {
      new URI("https://www.chatwork.com/")
    }
  }

  case class SessionContext(client: Client,
                            accessToken: String,
                            myId: String,
                            loginTime: Instant)

  private val accountsCache = mutable.HashMap[BigInt, Account]()

  private val sessionRooms = mutable.HashMap[BigInt, RoomMeta]()

  private var lastLoginAction: Option[Instant] = None

  private var currentContext: Option[SessionContext] = None

  private val logger = LoggerFactory.getLogger(getClass)

  def login: Either[Exception, SessionContext] = {
    lastLoginAction match {
      case Some(l) =>
        if (Duration.between(l, Instant.now).getSeconds < LOGIN_ACTION_SPAN) {
          return Left(
            ExceedQuotaException("Login action should be lower than one query per " + LOGIN_ACTION_SPAN + " seconds.")
          )
        }
      case _ =>
    }

    lastLoginAction = Some(Instant.now())

    val client = Client()

    val loginUri = orgId match {
      case Some(s) =>
        baseUri
          .withPath("/login.php")
          .withQuery("s" -> s)
          .withQuery("lang" -> "en")
          .withQuery("package" -> "chatwork")
      case _ =>
        baseUri
          .withPath("/login.php")
          .withQuery("lang" -> "en")
    }

    client.post(
      uri = loginUri,
      formData = List(
        "email" -> email,
        "password" -> password
      )
    ) match {
      case Left(e) => Left(e)
      case _ =>
        client.get(baseUri) match {
          case Left(e) => Left(e)
          case Right(r) =>
            val accessTokenRegex = """var ACCESS_TOKEN = '(\w+)';""".r
            val myIdRegex = """var myid = '(\d+)';""".r

            (accessTokenRegex.findFirstMatchIn(r.contentAsString), myIdRegex.findFirstMatchIn(r.contentAsString)) match {
              case (Some(token), Some(myId)) =>
                val sc = SessionContext(client, token.group(1), myId.group(1), Instant.now())
                currentContext = Some(sc)
                Right(sc)
              case _ =>
                Left(UnknownChatworkProtocolException("Unknown response sequence on `login`"))
            }
        }
    }
  }

  def contacts: List[Account] = {
    if (sessionRooms.size < 1) {
      // invoke init_load
      rooms
    }

    accountsCache.values.toList
  }

  def rooms: List[RoomMeta] = {
    if (sessionRooms.size > 0) {
      return sessionRooms.values.toList
    }

    api(
      "init_load",
      Map(
        "new" -> "1"
      )
    ) match {
      case Left(e) => throw e
      case Right(r) => {
        val result = r.asInstanceOf[Map[String, Any]]

        val contacts = result.get("contact_dat").get.asInstanceOf[Map[String, Map[String, Any]]].map {
          d =>
            Account.fromAccountDat(d._2)
        }.toList

        // update cache
        contacts.foreach(a => accountsCache.put(a.aid, a))

        val rooms = result.get("room_dat").get.asInstanceOf[Map[String, Map[String, Any]]].map {
          m =>
            RoomMeta(
              roomId = BigInt(m._1),
              description = m._2.get("n") match {
                case Some(n) => Some(n.asInstanceOf[String])
                case _ => None
              }
            )
        }.toList

        // update cache
        rooms.foreach(m => sessionRooms.put(m.roomId, m))

        rooms
      }
    }
  }

  def account(aid: BigInt): Option[Account] = {
    val r = accounts(List(aid))
    if (r.size < 1) {
      None
    } else {
      Some(r(0))
    }
  }

  def accounts(aid: List[BigInt]): List[Account] = {
    val aidOnCache = accountsCache.keys.toList.filter(aid.contains)
    val accountsOnCache = aidOnCache.flatMap(accountsCache.get)
    val aidNotOnCache = aid.filterNot(aidOnCache.contains)

    if (aidNotOnCache.size < 1) {
      return accountsOnCache
    }

    api(
      "get_account_info",
      Map(),
      Some(JSONObject(Map("aid" -> JSONArray(aidNotOnCache.distinct))))
    ) match {
      case Left(e) => throw e
      case Right(r) => {
        val result = r.asInstanceOf[Map[String, Any]]
        val accounts = result.get("account_dat").get.asInstanceOf[Map[String, Map[String, Any]]].map {
          d =>
            Account.fromAccountDat(d._2)
        }.toList

        // update cache
        accounts.foreach(a => accountsCache.put(a.aid, a))

        accounts ::: accountsOnCache
      }
    }
  }

  def messages(before: Message): List[Message] = {
    api(
      "load_old_chat",
      Map(
        "room_id" -> before.roomId.toString,
        "first_chat_id" -> before.messageId.toString
      )
    ) match {
      case Left(e) => throw e
      case Right(r) => {
        val result = r.asInstanceOf[Map[String, Any]]
        result.get("chat_list").get.asInstanceOf[List[Map[String, Any]]].map {
          c =>
            Message.fromChatList(before.roomId, c)
        }
      }
    }
  }

  def say(text: String, room: Room): Unit = {
    api(
      "send_chat",
      Map(),
      Some(
        JSONObject(
          Map(
            "text" -> text,
            "room_id" -> room.roomId.toString(),
            "last_chat_id" -> room.messages.last.messageId,
            "read" -> 1,
            "edit_id" -> 0
          )
        )
      )
    )
  }

  def room(roomMeta: RoomMeta): Option[Room] = {
    room(roomMeta.roomId)
  }

  def room(roomId: BigInt): Option[Room] = {
    api(
      "load_chat",
      Map(
        "room_id" -> roomId.toString,
        "last_chat_id" -> "0",
        "first_chat_id" -> "0",
        "jump_to_chat_id" -> "0",
        "unread_num" -> "0",
        "desc" -> "1",
        "task" -> "1"
      )
    ) match {
      case Left(e) => throw e
      case Right(r) => Some(Room.fromLoadChat(roomId, r.asInstanceOf[Map[String, Any]]))
    }
  }

  def api(command: String,
          params: Map[String, String],
          data: Option[JSONObject] = None): Either[Exception, Any] = {
    throttle.execute {
      () => {
        val context = currentContext match {
          case None => login match {
            case Left(e) => return Left(e)
            case Right(c) => c
          }
          case Some(c) => c
        }

        val gatewayUri = baseUri.withPath("/gateway.php")
          .withQuery("cmd", command)
          .withQuery(params.toList)
          .withQuery("myid", context.myId)
          .withQuery("_v", "1.80a")
          .withQuery("_av", "4")
          .withQuery("_t", context.accessToken)
          .withQuery("ln", "en")

        val response = data match {
          case Some(d) =>
            context.client.post(
              gatewayUri,
              List("pdata" -> d.toString())
            )
          case _ => context.client.get(gatewayUri)
        }

        response match {
          case Left(e) => Left(e)
          case Right(r) =>
            logger.debug(r.contentAsString)

            JSON.perThreadNumberParser = {
              number: String => BigInt(number)
            }
            JSON.parseFull(r.contentAsString) match {
              case Some(json) =>
                try {
                  val jsonObj = json.asInstanceOf[Map[String, Any]]
                  val status = jsonObj.get("status").get.asInstanceOf[Map[String, Any]]
                  if (status.get("success").get.asInstanceOf[Boolean]) {
                    Right(jsonObj.get("result").get)
                  } else {
                    Left(CommandFailureException(command, status.get("message").get.asInstanceOf[String]))
                  }
                } catch {
                  case e: Exception => Left(e)
                }
              case _ =>
                Left(UnknownChatworkProtocolException("invalid JSON format result for command [" + command + "]"))
            }
        }
      }
    }
  }
}

object Session {
  def apply(email: String, password: String): Session = Session(email, password, None)

  def apply(email: String, password: String, orgId: String): Session = Session(email, password, Some(orgId))
}
