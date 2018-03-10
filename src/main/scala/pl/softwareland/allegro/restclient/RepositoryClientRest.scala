package pl.softwareland.allegro.restclient


import pl.softwareland.allegro._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import pl.softwareland.allegro.model.{BadRequestMessage, RepositoryResponse}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
class RepositoryClientRest(userName: String, repositoryName: String) {

  def getRepositoryData(implicit executionContext: ExecutionContext, system: ActorSystem):Future[Either[BadRequestMessage, RepositoryResponse]] = {


    implicit val materializer = ActorMaterializer()

    val EXCEPTION = "exception"

    val api = config.getString("services.api.url")

    val uri = s"${api}${userName}/${repositoryName}"
    val reqEntity = Array[Byte]()

    import pl.softwareland.allegro.unmarshaller.RepositoryResponseUnmashaller._

    val respEntity = for {
      request <- Marshal(reqEntity).to[RequestEntity]
      response <- Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = uri, entity = request))
      ent <- response.status match {
        case OK => Unmarshal(response.entity).to[RepositoryResponse].map(Right(_))
        case BadRequest => Future.successful(Left(BadRequestMessage(BadRequest.reason, BadRequest.defaultMessage)))
        case _ =>  Future.successful(Left(BadRequestMessage(response.status.reason(), response.status.defaultMessage())))
      }
    } yield ent

    respEntity andThen {
      case Success(entity) => entity
      case Failure(ex) =>
        Left(BadRequestMessage(EXCEPTION, ex.getMessage))
    }
  }
}

object RepositoryClientRest {
  def apply(userName: String, repositoryName: String): RepositoryClientRest = new RepositoryClientRest(userName, repositoryName)
}


