package pl.softwareland.allegro.service

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.{Directives, Route}
import pl.softwareland.allegro.model.Repository
import pl.softwareland.allegro.restclient.RepositoryClientRest
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

import scala.concurrent.ExecutionContext

object RepositoryService extends Directives {

    import DefaultJsonProtocol._

    implicit val printer = PrettyPrinter
    implicit val repositoryFormat = jsonFormat5(Repository.apply)

  def getRepositoryService(implicit executionContext: ExecutionContext, actor: ActorSystem): Route = {
    pathPrefix("repositories") {
      path(Segments) { params => {
        get {
          complete {
               params match {
                 case Nil => "bad request"
                 case head::tail => getClient(head, tail.head)
               }
            }
          }
        }
      }
      }
    }

  private def getClient(user:String, repositoryName:String)(implicit executionContext: ExecutionContext, actor: ActorSystem) ={
    RepositoryClientRest(user, repositoryName).getRepositoryData.map(s => {
      Repository(s.full_name, s.description, s.clone_url, s.stargazers_count.toInt, s.created_at)
    })
  }
}


object JsonSupport {

  import DefaultJsonProtocol._

  implicit val printer = PrettyPrinter
  implicit val repositoryFormat = jsonFormat5(Repository)
}