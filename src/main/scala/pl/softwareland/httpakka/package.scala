package pl.softwareland

import com.typesafe.config.ConfigFactory

package object httpakka {

  lazy val config = ConfigFactory.load()

}