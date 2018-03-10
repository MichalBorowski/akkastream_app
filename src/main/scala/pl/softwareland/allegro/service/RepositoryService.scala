package pl.softwareland.allegro.service

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.{Directives, Route}
import pl.softwareland.allegro.model.{BadRequestMessage, Repository}
import pl.softwareland.allegro.restclient.RepositoryClientRest

import scala.concurrent.ExecutionContext

object RepositoryService extends Directives {

  import pl.softwareland.allegro.marshalller.JsonSupport._

  def getRepositoryService(implicit executionContext: ExecutionContext, actor: ActorSystem): Route = {
    pathPrefix("repositories") {
      path(Segments) { params => {
        get {
          complete {
            params match {
              case Nil => "no parameters provided in url"
              case params if(params.size < 2) => "give more parameters than one"
              case head::tail => getClient(head, tail.head)
            }
          }
        }
      }
      }
    }
  }

  private def getClient(user: String, repositoryName: String)(implicit executionContext: ExecutionContext, actor: ActorSystem) = {
    RepositoryClientRest(user, repositoryName).getRepositoryData.map[ToResponseMarshallable]( b => {
      b match {
        case Right(rr) => Repository(rr.full_name, rr.description, rr.clone_url, rr.stargazers_count.toInt, rr.created_at)
        case Left(bd) => BadRequestMessage(bd.resonse, bd.message)
      }
    })
  }
}

