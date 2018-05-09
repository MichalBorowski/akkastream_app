package pl.softwareland.akkastream.service

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import org.scalatest.{FlatSpec, Matchers}
import pl.softwareland.akkastream.model.{BadRequestMessage, Repository}

import scala.concurrent.duration._
class RepositoryServiceTest extends FlatSpec  with Matchers with ScalatestRouteTest{

   import pl.softwareland.akkastream.marshalller.JsonSupport._

  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(5.second)


  val repository = Repository("MichalBorowski/test", "",
    "https://github.com/MichalBorowski/test.git", 0, "2018-01-23T10:01:51Z")


  val badRequest = BadRequestMessage("Not Found", "The requested resource could not be found but may be available again in the future.")

  "Service" should "respond to single request" in {
    Get("/repositories/MichalBorowski/test") ~> RepositoryService.getRepositoryService ~> check {
      status shouldEqual OK
      responseAs[Repository] shouldEqual repository
    }
  }
  "Service" should "respond to bad request" in {
    Get("/repositories/MichalBorowski/te") ~> RepositoryService.getRepositoryService ~> check {
      responseAs[BadRequestMessage] shouldEqual badRequest
    }
  }

  "Service" should "show message give more parameters than one" in {
    Get("/repositories/MichalBorowski") ~> RepositoryService.getRepositoryService ~> check {
      responseAs[String] shouldEqual "give more parameters than one"
    }
  }
}