package etude.chatwork.repository.api.v0

import etude.http._
import etude.qos.Retry.retry
import java.net.URI
import java.time.{Duration, Instant}
import org.slf4j.LoggerFactory
import scala.collection.mutable
import scala.util.parsing.json.JSON
import scala.Some
import etude.http.Client
import scala.util.parsing.json.JSONArray
import scala.util.parsing.json.JSONObject
import etude.qos.Throttle

case class Session(email: String,
                   password: String,
                   orgId: Option[String]) {
  /**
   * Wait login action for seconds.
   */
  lazy val LOGIN_ACTION_SPAN = 10

  lazy val MAX_RETRIES = 3

  lazy val throttle: Throttle = Throttle(maxQueryPerSecond = 0.5, randomWaitRangeSeconds = 4)

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

  lazy val imageBaseUri: URI = new URI("https://tky-chat-work-appdata.s3.amazonaws.com/avatar/")

  case class SessionContext(client: Client,
                            accessToken: String,
                            myId: String,
                            loginTime: Instant)

  private val accountsCache = mutable.HashMap[AccountId, Account]()

  private val sessionRooms = mutable.HashMap[RoomId, RoomMeta]()

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
                Left(LoginFailedException("Invalid email or password"))
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

        val contacts = result.get("contact_dat").get.asInstanceOf[Map[String, Map[String, Any]]].flatMap {
          d =>
            try {
              Some(Account.fromAccountDat(d._2))
            } catch {
              case e: Exception =>
                None
            }
        }.toList

        // update cache
        contacts.foreach(a => accountsCache.put(a.aid, a))

        val rooms = result.get("room_dat").get.asInstanceOf[Map[String, Map[String, Any]]].map {
          m =>
            RoomMeta(
              roomId = RoomId(BigInt(m._1)),
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

  def account(aid: AccountId): Option[Account] = {
    val r = accounts(List(aid))
    if (r.size < 1) {
      None
    } else {
      Some(r(0))
    }
  }

  def accountsFromRoles(roles: List[Role]): List[Account] = {
    accounts(roles.map(_.aid))
  }

  def accounts(aid: List[AccountId]): List[Account] = {
    val aidOnCache = accountsCache.keys.toList.filter(aid.contains)
    val accountsOnCache = aidOnCache.flatMap(accountsCache.get)
    val aidNotOnCache = aid.filterNot(aidOnCache.contains)

    if (aidNotOnCache.size < 1) {
      return accountsOnCache
    }

    api(
      "get_account_info",
      Map(),
      Some(JSONObject(Map("aid" -> JSONArray(aidNotOnCache.distinct.map(_.accountId)))))
    ) match {
      case Left(e) => throw e
      case Right(r) => {
        val result = r.asInstanceOf[Map[String, Any]]
        val accounts = result.get("account_dat").get.asInstanceOf[Map[String, Map[String, Any]]].flatMap {
          d =>
            try {
              Some(Account.fromAccountDat(d._2))
            } catch {
              case e: Exception => {
                None
              }
            }
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
        "room_id" -> before.roomId.roomId,
        "first_chat_id" -> before.messageId.messageId
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
            "room_id" -> room.roomId.roomId,
            "last_chat_id" -> room.messages.last.messageId.messageId,
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

  def room(roomId: RoomId): Option[Room] = {
    api(
      "load_chat",
      Map(
        "room_id" -> roomId.roomId,
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

  def addAttendees(roomId: RoomId, newAttendees: List[Role]): List[Role] = {
    val totalAttendees = attendees(roomId) ++ newAttendees
    updateAttendees(roomId, totalAttendees)
  }

  def updateAttendees(roomId: RoomId, attendees: List[Role]): List[Role] = {
    api(
      "update_room",
      Map(),
      Some(
        JSONObject(
          Map(
            "room_id" -> roomId.roomId,
            "role" -> JSONObject(
              attendees.map(a => a.aid.toString -> a.roleName).toMap
            )
          )
        )
      )
    ) match {
      case Left(e) => throw e
      case Right(r) => attendees
    }
  }

  def markAsRead(room: Room): Unit = {
    room.lastChatMessage match {
      case Some(m) => markAsRead(room.roomId, m)
      case _ =>
      // nothing to do
    }
  }

  def markAsRead(roomId: RoomId, message: Message): Unit = {
    api(
      "read",
      Map(
        "room_id" -> roomId.roomId,
        "last_chat_id" -> message.messageId.messageId
      )
    ) match {
      case Left(e) => throw e
      case Right(r) =>
      // response is like :  {"status":{"success":true},"result":{"read_num":372,"mention_num":0}}
    }
  }

  def attendees(roomId: RoomId): List[Role] = {
    api(
      "get_room_info",
      Map(),
      Some(
        JSONObject(
          Map(
            "type" -> "",
            "rid" -> roomId.roomId,
            "t" -> JSONObject(
              Map(
                roomId.toString -> 1
              )
            ),
            "d" -> JSONArray(List(roomId.roomId)),
            "m" -> JSONArray(List(roomId.roomId)),
            "p" -> JSONArray(List(roomId.roomId)),
            "i" -> JSONObject(
              Map(
                roomId.roomId -> JSONObject(
                  Map(
                    "t" -> 0,
                    "l" -> 0,
                    "u" -> 0,
                    "c" -> 0
                  )
                )
              )
            )
          )
        )
      )
    ) match {
      case Left(e) => throw e
      case Right(r) =>
        val roomDat = r.asInstanceOf[Map[String, Any]].get("room_dat").get.asInstanceOf[Map[String, Any]]
        val roomInfo = roomDat.get(roomId.roomId).get.asInstanceOf[Map[String, Any]]
        Role.fromRoomInfo(roomInfo.get("m").get.asInstanceOf[Map[String, BigInt]])
    }
  }

  private def apiUri(command: String, params: Map[String, String], data: Option[JSONObject], context: SessionContext): URI = {
    baseUri.withPath("/gateway.php")
      .withQuery("cmd", command)
      .withQuery(params.toList)
      .withQuery("myid", context.myId)
      .withQuery("_v", "1.80a")
      .withQuery("_av", "4")
      .withQuery("_t", context.accessToken)
      .withQuery("ln", "en")
      .withQuery("_", System.currentTimeMillis().toString)
  }

  private def apiResponseParser(command: String, response: Response): Either[Exception, Any] = {
    JSON.perThreadNumberParser = {
      number: String => BigInt(number)
    }
    JSON.parseFull(response.contentAsString) match {
      case Some(json) =>
        try {
          val jsonObj = json.asInstanceOf[Map[String, Any]]
          val status = jsonObj.get("status").get.asInstanceOf[Map[String, Any]]
          if (status.get("success").get.asInstanceOf[Boolean]) {
            Right(jsonObj.get("result").get)
          } else {
            status.get("message") match {
              case Some(msg: String) =>
                if (msg.contains("NO LOGIN")) {
                  Left(SessionTimeoutException(msg))
                } else {
                  Left(CommandFailureException(command, msg))
                }
              case _ =>
                Left(CommandFailureException(command, response.contentAsString))
            }
          }
        } catch {
          case e: Exception => Left(e)
        }
      case _ =>
        Left(UnknownChatworkProtocolException("invalid JSON format result for command [" + command + "]",
          response.contentAsString))
    }
  }

  def api(command: String,
          params: Map[String, String],
          data: Option[JSONObject] = None): Either[Exception, Any] = {
    retry(MAX_RETRIES, {
      case t: SessionTimeoutException => true
      case _ => false
    }) {
      throttle.execute {
        val context = currentContext match {
          case Some(c) => c
          case None => login match {
            case Left(e) => return Left(e)
            case Right(c) => c
          }
        }
        val gatewayUri = apiUri(command, params, data, context)
        val response = data match {
          case Some(d) => context.client.post(gatewayUri, List("pdata" -> d.toString()))
          case _ => context.client.get(gatewayUri)
        }
        response match {
          case Left(e) => Left(e)
          case Right(r) => apiResponseParser(command, r)
        }
      }
    }
  }
}

object Session {
  def apply(email: String, password: String): Session = Session(email, password, None)

  def apply(email: String, password: String, orgId: String): Session = Session(email, password, Some(orgId))
}
