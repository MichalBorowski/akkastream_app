package pl.softwareland.httpakka.service

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import org.scalatest.{FlatSpec, Matchers}
import pl.softwareland.httpakka.model.{BadRequestMessage, Repository}

import scala.concurrent.duration._
class RepositoryServiceTest extends FlatSpec  with Matchers with ScalatestRouteTest{

   import pl.softwareland.httpakka.marshalller.JsonSupport._

  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(5.second)


  val repository = Repository("softwareland/spark", "Apache Spark",
    "https://github.com/softwareland/spark.git", 0, "2019-03-21T11:23:18Z")


  val badRequest = BadRequestMessage("Not Found", "The requested resource could not be found but may be available again in the future.")

  "Service" should "respond to single request" in {
    Get("/repositories/softwareland/spark") ~> RepositoryService.getRepositoryService ~> check {
      status shouldEqual OK
      responseAs[Repository] shouldEqual repository
    }
  }
  "Service" should "respond to bad request" in {
    Get("/repositories/softwareland/spar") ~> RepositoryService.getRepositoryService ~> check {
      responseAs[BadRequestMessage] shouldEqual badRequest
    }
  }

  "Service" should "show message give more parameters than one" in {
    Get("/repositories/softwareland") ~> RepositoryService.getRepositoryService ~> check {
      responseAs[String] shouldEqual "give more parameters than one"
    }
  }
}