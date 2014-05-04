scalaVersion := "2.10.3"
 
sbtVersion := "0.13.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"         % "2.3.2",
  "io.spray"          %  "spray-can"          % "1.3.1",
  "io.spray"          %  "spray-routing"      % "1.3.1"
)
