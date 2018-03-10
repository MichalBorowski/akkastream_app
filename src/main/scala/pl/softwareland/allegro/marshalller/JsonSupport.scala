package pl.softwareland.allegro.marshalller

import pl.softwareland.allegro.model.Repository
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

object JsonSupport {


  import DefaultJsonProtocol._

  implicit val printer = PrettyPrinter
  implicit val repositoryFormat = jsonFormat5(Repository.apply)
}
