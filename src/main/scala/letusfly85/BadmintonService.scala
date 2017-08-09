package letusfly85

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import kamon.Kamon
import scala.concurrent.ExecutionContextExecutor

trait BadmintonService {

  implicit val system: ActorSystem
  implicit val executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  val badmintonActor: ActorRef

  implicit val timeout = Timeout(5000, TimeUnit.MILLISECONDS)

  def logger: LoggingAdapter

  Kamon.start()

  val routes =
    path("v1" / "status") {
      (get) {
        logger.info("/v1/status")

        badmintonActor ! "service over, one point!"
        complete("ok")
      }
    }
}

object BadmintonService extends App with BadmintonService {
    override implicit val system: ActorSystem  = ActorSystem("AwesomeBadminton")
    override implicit val executor: ExecutionContextExecutor = system.dispatcher
    override implicit val materializer: Materializer = ActorMaterializer()

    override val logger = Logging(system, getClass)

    override val badmintonActor: ActorRef = system.actorOf(Props(classOf[BadmintonActor]), "badminton-actor")

    Http().bindAndHandle(routes, "0.0.0.0", 8080)
}
