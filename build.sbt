ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "rockthejvm-fs2-article"
  )

libraryDependencies += "co.fs2" %% "fs2-core" % "3.11.0"

