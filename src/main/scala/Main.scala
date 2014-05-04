import akka.io.IO
import spray.can.Http
import akka.actor.ActorSystem
import akka.actor.Props

object Main {
  def main(args: Array[String]) {
    implicit val actorSystem = ActorSystem("example")
    IO(Http) ! Http.Bind(actorSystem.actorOf(Props[Service], name = "service"), interface = "0.0.0.0", port = 8080)
  }
}