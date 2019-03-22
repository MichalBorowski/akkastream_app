package pl.softwareland.httpakka.marshalller

import pl.softwareland.httpakka.model.Repository
import pl.softwareland.httpakka.model.BadRequestMessage
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

object JsonSupport {

  import DefaultJsonProtocol._

  implicit val printer = PrettyPrinter
  implicit val repositoryFormat = jsonFormat5(Repository.apply)
  implicit val badRequestMessageFormat = jsonFormat2(BadRequestMessage.apply)
}
