package pl.softwareland.allegro.service

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import pl.softwareland.allegro.model.Repository
import pl.softwareland.allegro.service.RepositoryService.{Segments, complete, get, getClient, path, pathPrefix}

import scala.concurrent.Future

class RepositoryServiceTest extends WordSpec with Matchers with ScalatestRouteTest {

  import pl.softwareland.allegro.marshalller.JsonSupport._

  val repository = Repository("mm", "aa", "bb", 0, "cc")

  val route = pathPrefix("repositories") {
    path("MichalBorowski/test"){
      get {
        complete(Future.successful(ToResponseMarshallable(repository)))
      }
    }
  }


  "The service" should {

      "return a greeting for GET requests to the root path" in {
        // tests:

        Get() ~> route ~> check {
          responseAs[Repository] shouldEqual repository
        }
      }
    }

}