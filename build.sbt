lazy val root = (project in file(".")).
  settings(
    name := "noredink",
    version := "0.1",
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq("-deprecation", "-feature")
  )

connectInput in run := true
