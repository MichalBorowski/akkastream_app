package pl.softwareland.allegro.restclient

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._

class RepositoryClientRest(userName: String, repositoryName: String) {


  def getRepositoryData(implicit executionContext: ExecutionContext, system: ActorSystem) = {

    implicit val materializer = ActorMaterializer()

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = s"https://api.github.com/repos/${userName}/${repositoryName}"))

    responseFuture andThen {
      case Success(res) => println(Unmarshal(res.entity.toStrict(3.seconds)).value)
      case Failure(_) => sys.error("errer")
    }
  }
}

object RepositoryClientRest {
  def apply(userName: String, repositoryName: String): RepositoryClientRest = new RepositoryClientRest(userName, repositoryName)
}
