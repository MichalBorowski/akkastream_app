package pl.softwareland.httpakka.unmarshaller

import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, PredefinedFromEntityUnmarshallers}
import pl.softwareland.httpakka.model.RepositoryResponse

object RepositoryResponseUnmashaller {

  private def parseOpt(s: String): Option[RepositoryResponse] = {

    util.parsing.json.JSON.parseFull(s) match {
      case Some(map: Map[String, Any]@unchecked) =>
        Some(RepositoryResponse(
          Option(map("full_name").asInstanceOf[String]) match {
            case Some(full) => full
            case _ => ""
          },
          Option(map("description").asInstanceOf[String]) match {
            case Some(d) => d
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
    PredefinedFromEntityUnmarshallers.stringUnmarshaller.map { s =>
      parseOpt(s).getOrElse {
        throw new RuntimeException(s"Unknown RepositoryResponse: $s")
      }
    }
  }
}
