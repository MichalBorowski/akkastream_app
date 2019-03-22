package pl.softwareland.httpakka

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import pl.softwareland.httpakka.service.RepositoryService

import scala.util.{Failure, Success}

object ApplicationStarter extends App {

  val GitWebSystem = "git_web_system"

  implicit val system = ActorSystem(GitWebSystem)
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher

  val host = config.getString("http.host")

  val port =config.getInt("http.port")

  val route = RepositoryService.getRepositoryService

  val bindingFuture = Http().bindAndHandle(route, host, port)

  val log = Logging(system.eventStream, GitWebSystem)

  bindingFuture.onComplete {
    case Success(server) =>
      log.info(s"Server run on ${server.localAddress}")
    case Failure(exception) =>
      log.error(exception, "system failure")
      system.terminate()
  }
}
