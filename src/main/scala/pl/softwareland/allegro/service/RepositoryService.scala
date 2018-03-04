package pl.softwareland.allegro.service

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import pl.softwareland.allegro.model.service.Repository
import pl.softwareland.allegro.restclient.RepositoryClientRest

object RepositoryService {

  def getRepositoryService : Route= {
    val route =
        pathPrefix("repositories") {
          path("owner"){ put {
            entity(as[String]) { s =>
              complete {
                RepositoryClientRest("MichalBorowski", "test").getRepositoryData
              }
            }
          }
          }
        }


  }


}
