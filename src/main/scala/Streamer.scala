import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import scala.util.Random
import spray.http.ChunkedResponseStart
import spray.http.HttpResponse
import spray.can.Http
import spray.http.MessageChunk
import spray.http.ChunkedMessageEnd
import spray.http.DateTime

class Streamer(responder: ActorRef) extends Actor with ActorLogging {
  case class Ok(remaining: Int)

  override def preStart() {
    // very important that ChunkedResponseStart has a non-empty entity, otherwise compression isn't activated 
    responder ! ChunkedResponseStart(HttpResponse(entity = List.fill(1024)(" ").mkString(""))).withAck(Ok(50))
  }

  def receive = {
    case Ok(percent) =>
      Thread.sleep(500)
      val number = Math.min(Math.abs(if (Random.nextBoolean()) { percent + Random.nextInt(5) } else { percent - Random.nextInt(5)}) , 100)

      val data = "{ \"count\": "+number+" }"
      val nextChunk = MessageChunk(data)
      responder ! nextChunk.withAck(Ok(number))

    case m: Http.ConnectionClosed =>
      log.warning(s"Stopping response streaming due to ${m}")
  }
}