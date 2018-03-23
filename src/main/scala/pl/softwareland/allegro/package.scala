package pl.softwareland

import com.typesafe.config.ConfigFactory

package object allegro {

  lazy val config = ConfigFactory.load()

}
