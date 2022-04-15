name := "EnelXInterview"

version := "1.0"

lazy val `enelxinterview` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test"
)
