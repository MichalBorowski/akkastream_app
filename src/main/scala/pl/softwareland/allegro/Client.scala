package pl.softwareland.allegro

import akka.NotUsed
import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{HttpCookie, `Set-Cookie`}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream._
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object Client extends App{

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val uri = "https://api.github.com/repos/MichalBorowski/test"
  val reqEntity = Array[Byte]()

  val respEntity = for {
    request <- Marshal(reqEntity).to[RequestEntity]
    response <- Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = uri, entity = request))
    entity <- Unmarshal(response.entity).to[ByteString]
  } yield entity

  val payload = respEntity.andThen {
    case Success(entity) =>
      println(s"""{"content": "${entity.utf8String}"}""")
    case Failure(ex) =>
      println(s"""{"error": "${ex.getMessage}"}""")
  }
}