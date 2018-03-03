package pl.softwareland.allegro.restclient
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class RepositoryClientRest(userName:String, repositoryName:String){


  def getRepositoryData(implicit executionContext: ExecutionContext, system: ActorSystem){

    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(uri = s"https://api.github.com/repos/${userName}/${repositoryName}"))

    responseFuture
      .onComplete {
        case Success(res) =>
          println(res.entity)
        case Failure(_) => sys.error("something wrong")
      }
  }
}
object RepositoryClientRest {
  def apply(userName: String, repositoryName: String): RepositoryClientRest = new RepositoryClientRest(userName, repositoryName)
}
