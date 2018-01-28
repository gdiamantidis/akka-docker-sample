import sbt.Keys._
import sbtassembly.AssemblyPlugin.autoImport._

name := "gdiama-akka-cluster-docker-sample"
version := "0.1"
scalaVersion := "2.11.7"
lazy val akkaVersion = "2.5.3"
fork in Test := true

enablePlugins(JavaAppPackaging)
//enablePlugins(AshScriptPlugin)
//dockerBaseImage       := "openjdk:jre-alpine"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.github.scopt" %% "scopt" % "3.2.0",

  "com.typesafe.akka" %% "akka-http" % "10.0.9",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.9",
  "org.scala-lang" % "scala-library" % "2.11.7",

  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "commons-io" % "commons-io" % "2.4" % "test")

val mainClassSt = "gdiama.cluster.Main"

lazy val runSeed = taskKey[Unit]("Start the seed node on 127.0.0.1:2551")
fullRunTask(runSeed, Compile, mainClassSt, "--seed")
fork in runSeed := true

javaOptions in runSeed ++= Seq(
  "-Dclustering.ip=127.0.0.1",
  "-Dclustering.port=2551"
)

lazy val runNode = taskKey[Unit]("Start a node on 127.0.0.1:2552")
fullRunTask(runNode, Compile, mainClassSt, "127.0.0.1:2551")
fork in runNode := true

javaOptions in runNode ++= Seq(
  "-Dclustering.ip=127.0.0.1",
  "-Dclustering.port=2552"
)

//lazy val commonSettings = Seq(
//  version := "0.1-SNAPSHOT",
//  organization := "com.example",
//  test in assembly := {}
//)

mappings in Universal := {
  // universalMappings: Seq[(File,String)]
  val universalMappings = (mappings in Universal).value
  val fatJar = (assembly in Compile).value
  // removing means filtering
  val filtered = universalMappings filter {
    case (file, name) => !name.endsWith(".jar")
  }
  // add the fat jar
  filtered :+ (fatJar -> ("lib/" + fatJar.getName))
}

//mainClass in Compile := Some(mainClassSt)
//mainClass in assembly := Some(mainClassSt)
assemblyJarName in assembly := "sample-server-clustered-exec.jar"

scriptClasspath := Seq((assemblyJarName in assembly).value)

