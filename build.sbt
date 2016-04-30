name := "neo4j4s"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.neo4j" % "neo4j-kernel" % "3.0.0",
  "org.neo4j.driver" % "neo4j-java-driver" % "1.0.0",
  "com.typesafe" % "config" % "1.3.0"
)