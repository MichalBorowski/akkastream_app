package pl.softwareland.allegro.restclient

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import pl.softwareland.allegro.model.RepositoryResponse

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
class RepositoryClientRest(userName: String, repositoryName: String) {


  def getRepositoryData(implicit executionContext: ExecutionContext, system: ActorSystem):Future[RepositoryResponse] = {

    implicit val materializer = ActorMaterializer()

    val uri = s"https://api.github.com/repos/${userName}/${repositoryName}"
    val reqEntity = Array[Byte]()

    import pl.softwareland.allegro.unmarshaller.RepositoryResponseUnmashaller._

    val respEntity = for {
      request <- Marshal(reqEntity).to[RequestEntity]
      response <- Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = uri, entity = request))
      entity <- Unmarshal(response.entity).to[RepositoryResponse]
    } yield entity

    respEntity andThen {
      case Success(entity) =>
        entity
      case Failure(ex) =>
        RepositoryResponse(ex.getMessage, "", "", 0, "")
    }
  }
}

object RepositoryClientRest {
  def apply(userName: String, repositoryName: String): RepositoryClientRest = new RepositoryClientRest(userName, repositoryName)
}

