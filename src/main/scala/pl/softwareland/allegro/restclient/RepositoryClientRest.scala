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


  def getRepositoryData(implicit executionContext: ExecutionContext, system: ActorSystem):Future[ByteString] = {

    implicit val materializer = ActorMaterializer()

    val uri = s"https://api.github.com/repos/${userName}/${repositoryName}"
    val reqEntity = Array[Byte]()

    val respEntity = for {
      request <- Marshal(reqEntity).to[RequestEntity]
      response <- Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = uri, entity = request))
      entity <- Unmarshal(response.entity).to[ByteString]
    } yield entity

    respEntity andThen {
      case Success(entity) =>
       entity.utf8String
      case Failure(ex) =>
        ex.getMessage
    }
//    response.map(_.decodeString("UTF-8")).value match {
//      case Some(t) => t.get
//      case None => "somefing wrong"
//    }
  }
}

object RepositoryClientRest {
  def apply(userName: String, repositoryName: String): RepositoryClientRest = new RepositoryClientRest(userName, repositoryName)
}
