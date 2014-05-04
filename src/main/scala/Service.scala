import java.io.File
import spray.http.{AllOrigins, HttpOrigin, AllowedOrigins}
import spray.http.HttpHeaders.{`Access-Control-Allow-Headers`, `Access-Control-Allow-Origin`}
import spray.httpx.encoding.Gzip
import spray.routing.HttpServiceActor
import akka.actor.ActorLogging
import akka.actor.Props
import spray.routing.RequestContext

class Service() extends HttpServiceActor with ActorLogging {

  def receive = runRoute(route)

  val route = {
    // this is an example of allowing testing in localhost (this method is not a good idea!)
    options {
      respondWithHeader(`Access-Control-Allow-Origin`(AllOrigins)) {
        respondWithHeader(`Access-Control-Allow-Headers`("x-requested-with")) {
          complete("")
        }
      }
    } ~
    get {
      path("stream" / Rest) {
        client =>
          compressResponseIfRequested() {
            sendStreamingResponse(client)
          }
      } ~
      // this is an example of how to provide file from server rather than localhost (this method needs a lot of work!)
      path("oboe-browser.min.js") {
        compressResponse(Gzip) {
          val f = new File(getClass.getResource("oboe-browser.min.js").getPath)
          getFromFile(f)
        }
      }
    }
  }
  
  def sendStreamingResponse(client: String)(requestContext: RequestContext): Unit = {
    actorRefFactory.actorOf(Props(new Streamer(requestContext.responder)), name = s"streamer-${System.nanoTime()}")
  }
}