name := "allegro_app"

scalaVersion := "2.11.11"

version := "0.1"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.0",
  "com.typesafe.akka" %% "akka-stream" % "2.5.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.0-RC2",
  "org.json4s" %% "json4s-native" % "3.5.3",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.11" % Test,
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
