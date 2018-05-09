package pl.softwareland

import com.typesafe.config.ConfigFactory

package object akkastream {

  lazy val config = ConfigFactory.load()

}
