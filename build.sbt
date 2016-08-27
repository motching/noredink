lazy val root = (project in file(".")).
  settings(
    name := "noredink",
    version := "0.1",
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq("-deprecation", "-feature"),
    libraryDependencies += "com.github.scala-incubator.io" % "scala-io-file_2.11" % "0.4.3-1"
  )
