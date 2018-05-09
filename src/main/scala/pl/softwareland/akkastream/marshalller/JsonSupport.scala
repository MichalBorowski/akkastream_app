package pl.softwareland.akkastream.marshalller

import pl.softwareland.akkastream.model.{BadRequestMessage, Repository}
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

object JsonSupport {

  import DefaultJsonProtocol._

  implicit val printer = PrettyPrinter
  implicit val repositoryFormat = jsonFormat5(Repository.apply)
  implicit val badRequestMessageFormat = jsonFormat2(BadRequestMessage.apply)
}
