package pl.softwareland.allegro.restclient

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, PredefinedFromEntityUnmarshallers, Unmarshal}
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
class RepositoryClientRest(userName: String, repositoryName: String) {


  def getRepositoryData(implicit executionContext: ExecutionContext, system: ActorSystem):Future[RepositoryResponse] = {

    implicit val materializer = ActorMaterializer()

    val uri = s"https://api.github.com/repos/${userName}/${repositoryName}"
    val reqEntity = Array[Byte]()

    import RepositoryResponse._

    val respEntity = for {
      request <- Marshal(reqEntity).to[RequestEntity]
      response <- Http().singleRequest(HttpRequest(method = HttpMethods.GET, uri = uri, entity = request))
      entity <- Unmarshal(response.entity).to[RepositoryResponse]
    } yield entity

    respEntity andThen {
      case Success(entity) =>
       entity
      case Failure(ex) =>
        RepositoryResponse(ex.getMessage, "", "", 0, "")
    }
  }
}

object RepositoryClientRest {
  def apply(userName: String, repositoryName: String): RepositoryClientRest = new RepositoryClientRest(userName, repositoryName)
}

case class RepositoryResponse(full_name:String,
                              description:String,
                              clone_url:String,
                              stargazers_count:Double,
                              created_at:String)

object RepositoryResponse {

  private def parseOpt(s: String): Option[RepositoryResponse] = {

  util.parsing.json.JSON.parseFull(s) match {
      case Some(map: Map[String, Any] @unchecked) =>
          Some(RepositoryResponse(
            Option(map("full_name").asInstanceOf[String]) match {
              case Some(full) => full
              case _ => ""
            },
            Option(map("description").asInstanceOf[String]) match {
              case Some(d) =>d
              case _ => ""
            },
            Option(map("clone_url").asInstanceOf[String]) match {
              case Some(cu) => cu
              case _ => ""
            },
            Option(map("stargazers_count").asInstanceOf[Double]) match {
              case Some(sc) => sc
              case _ => 0
            },
            Option(map("created_at").asInstanceOf[String]) match {
            case Some(ca) => ca
            case _ => ""
          }))
      case _ => None
    }
  }

  implicit val unm: FromEntityUnmarshaller[RepositoryResponse] = {
    PredefinedFromEntityUnmarshallers.stringUnmarshaller.map{ s => parseOpt(s).getOrElse{
      throw new RuntimeException(s"Unknown RepositoryResponse: $s")
    }}
  }
}
