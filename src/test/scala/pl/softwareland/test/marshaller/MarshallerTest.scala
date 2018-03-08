package pl.softwareland.test.marshaller

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshaller.UnsupportedContentTypeException
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import akka.stream.ActorMaterializer
import akka.util.ByteString

class MarshallerTest extends AsyncWordSpec with Matchers with BeforeAndAfterAll{

  implicit val system = ActorSystem()
  implicit val mat    = ActorMaterializer()

  "unmarshaling bytestring object" in {
    val food = Food("bar", 100)
    val entity = HttpEntity("""{ "bar": "bar", "wiek":100, "lala":10 }""")
    Unmarshal(entity)
      .to[ByteString]
      .map(_ shouldBe ByteString("""{ "bar": "bar", "wiek":100, "lala":10 }"""))
  }

//  "unmarsaling custom objet" in {
//    val food = Food("bar", 100)
//    val entity = HttpEntity("""{ "bar": "bar", "wiek":100, "lala":10 }""")
//    Unmarshal(entity)
//      .to[Food]
//      .map(_ shouldBe food)
//  }


  case class Food(bar:String, wiek:Int)

}

