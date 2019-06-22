name := """simple-rest-scala"""

version := "1.0-SNAPSHOT"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)

resolvers ++= Seq(
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)


libraryDependencies += filters


libraryDependencies ++= Seq(
  ws
)

