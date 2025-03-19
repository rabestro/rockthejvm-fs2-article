ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "rockthejvm-fs2-article"
  )

libraryDependencies ++= Seq(
  "co.fs2"        %% "fs2-core"         % "3.11.0",
  "org.scalatest" %% "scalatest"        % "3.2.19" % Test,
)
