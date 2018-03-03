package pl.softwareland.allegro
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.util.{Failure, Success}


object Client {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "https://api.github.com/repos/MichalBorowski/test"))

    responseFuture
      .onComplete {
        case Success(res) => println(Unmarshal(res.entity).value)
        case Failure(_)   => sys.error("something wrong")
      }
  }
}
