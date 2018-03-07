package pl.softwareland.allegro.service

import java.util.Date

import akka.actor.ActorSystem
import akka.http.javadsl.server.PathMatchers
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.server.Directives._
import pl.softwareland.allegro.restclient.RepositoryClientRest
import org.json4s._
import org.json4s.native.JsonMethods._
import pl.softwareland.allegro.model.Repository
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
      val json = s.utf8String
      Repository(
        parse(json).\("full_name").values match {
          case JString(full_name) => full_name
          case _ => ""
        },
        parse(json).\("descrription").values match {
          case JString(s) => s
          case _ => ""
        },
        parse(json).\("clone_url").values match {
          case JString(cloneUrl) => cloneUrl
          case _ => ""
        },
        parse(json).\("stargazers_count").values match {
          case JInt(stars) => stars.toInt
          case _ => 0
        },
        parse(json).\("created_at").values match {
          case JString(createdAt) => createdAt
          case _ => ""
        })
    })
  }
}


object JsonSupport {

  import DefaultJsonProtocol._

  implicit val printer = PrettyPrinter
  implicit val repositoryFormat = jsonFormat5(Repository)
}