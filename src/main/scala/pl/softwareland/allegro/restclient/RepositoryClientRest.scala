package pl.softwareland.allegro.restclient

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.{Http, HttpsConnectionContext}
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.util.ByteString

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
class RepositoryClientRest(userName: String, repositoryName: String) {


  def getRepositoryData(implicit executionContext: ExecutionContext, system: ActorSystem) = {

    val uri = "https://api.github.com/repos/MichalBorowski/test"
    val reqEntity = Array[Byte]()

    val respEntity = for {
      request <- Marshal(reqEntity).to[RequestEntity]
      response <- Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = uri, entity = request))
      entity <- Unmarshal(response.entity).to[ByteString]
    } yield entity

    respEntity andThen {
      case Success(entity) =>
        println(s"""{"content": "${entity.utf8String}"}""")
      case Failure(ex) =>
        println(s"""{"error": "${ex.getMessage}"}""")
    }
  }
}

object RepositoryClientRest {
  def apply(userName: String, repositoryName: String): RepositoryClientRest = new RepositoryClientRest(userName, repositoryName)
}
