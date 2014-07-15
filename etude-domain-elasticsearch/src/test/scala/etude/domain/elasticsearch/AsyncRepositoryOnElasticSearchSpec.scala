package etude.domain.elasticsearch

import java.nio.file.{Files, Path}
import java.util.concurrent.{Executors, ExecutorService}

import etude.domain.core.lifecycle.async.{AsyncEntityIOContext, AsyncRepository}
import etude.domain.core.model.{Entity, Identity}
import etude.gazpacho.elasticsearch.Engine
import org.json4s.JsonAST._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods
import org.json4s.native.JsonMethods._
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

@RunWith(classOf[JUnitRunner])
class AsyncRepositoryOnElasticSearchSpec
  extends Specification {

  lazy val buildPath: Path = Files.createTempDirectory("domain-elasticsearch")

  lazy val engine: Engine = Engine.ofEmbedded("domain-elasticsearch", buildPath)

  val timeout = Duration(10, SECONDS)
  val executorsPool: ExecutorService = Executors.newCachedThreadPool()
  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

  "AsyncRepositoryOnElasticSearch" should {
    "store, get, then delete" in {
      implicit val ctx = new AsyncEntityIOContextImpl(executors)
      val repo = new AsyncRecipeRepositoryOnElasticSearch(engine)
      val recipe = new Recipe(RecipeId("Grilled corn"), "Place the corn on the hot grill")

      val prepare = Await.result(repo.prepare(), timeout)

      prepare must beTrue

      val store = Await.result(repo.store(recipe), timeout)

      store.identity must equalTo(recipe.identity)
      store.result must equalTo(repo)

      val get = Await.result(repo.resolve(recipe.identity), timeout)

      get.identity must equalTo(recipe.identity)
      get.recipe must equalTo(recipe.recipe)

      val exists = Await.result(repo.containsByIdentity(recipe.identity), timeout)

      exists must beTrue

      val delete = Await.result(repo.deleteByIdentity(recipe.identity), timeout)

      delete.identity must equalTo(recipe.identity)

      Await.result(repo.resolve(recipe.identity), timeout) must throwA[IllegalArgumentException]
      Await.result(repo.containsByIdentity(recipe.identity), timeout) must beFalse
    }
  }
}

case class AsyncEntityIOContextImpl(executor: ExecutionContext) extends AsyncEntityIOContext

case class RecipeId(name: String) extends Identity[String] {
  def value: String = name
}

class Recipe(val recipeId: RecipeId,
             val recipe: String) extends Entity[RecipeId] {
  val identity: RecipeId = recipeId
}

class AsyncRecipeRepositoryOnElasticSearch(val engine: Engine)
  extends AsyncRepository[RecipeId, Recipe]
  with AsyncRepositoryOnElasticSearch[RecipeId, Recipe] {
  type This <: AsyncRecipeRepositoryOnElasticSearch

  def marshal(entity: Recipe): String = {
    compact(
      render(
        ("name" -> entity.identity.name) ~
          ("recipe" -> entity.recipe)
      )
    )
  }

  def unmarshal(json: String): Recipe = {
    val p = JsonMethods.parse(json)
    val results: Seq[Recipe] = for {
      JObject(j) <- p
      JField("name", JString(name)) <- j
      JField("recipe", JString(recipe)) <- j
    } yield {
      new Recipe(
        RecipeId(name),
        recipe
      )
    }

    results.last
  }

  def idValue(identity: RecipeId): String = {
    s"recipe${identity.name.hashCode}"
  }

  def indexValue(identity: RecipeId): String = {
    s"recipe-${identity.name.hashCode % 4}"
  }

  override def indexValues(): Seq[String] = {
    Seq("recipe-0", "recipe-1", "recipe-2", "recipe-3")
  }

  val typeValue: String = "recipe"
}
