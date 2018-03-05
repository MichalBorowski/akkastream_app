package pl.softwareland.allegro.service

import java.util.Date

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import pl.softwareland.allegro.restclient.RepositoryClientRest
import org.json4s._
import org.json4s.native.JsonMethods._
import pl.softwareland.allegro.model.Repository

import scala.concurrent.ExecutionContext

object RepositoryService {


  def getRepositoryService(implicit executionContext: ExecutionContext, actor: ActorSystem): Route = {
      path("owner") {
        get {
          complete {
            RepositoryClientRest("MichalBorowski", "test").getRepositoryData.map(s => {
              val json = s.utf8String
              Repository(
                parse(json).\("full_name").values.toString,
                null,
                Some((parse(json)).\("clone_url").values.toString),
                0,
                new Date()).toString
            })
        }
      }
    }
 }
}