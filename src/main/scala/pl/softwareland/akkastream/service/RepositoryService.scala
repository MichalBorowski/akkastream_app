package pl.softwareland.akkastream.service

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Route
import pl.softwareland.akkastream.model.{BadRequestMessage, Repository}
import pl.softwareland.akkastream.restclient.RepositoryClientRest

import scala.concurrent.ExecutionContext

import akka.http.scaladsl.server.Directives._

object RepositoryService {

  import pl.softwareland.akkastream.marshalller.JsonSupport._

  def getRepositoryService(implicit executionContext: ExecutionContext, actor: ActorSystem): Route = {
    pathPrefix("repositories") {
      path(Segments) { params => {
        get {
          complete {
            params match {
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

