package club.diybio.bank

import akka.actor._
import akka.http.scaladsl.Http.{ServerBinding, IncomingConnection}

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl._

import akka.routing._
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Source
import HttpMethods._
import club.diybio.bank.templates.{MyStyles, Index}
import scalacss.Defaults._
import scala.concurrent.Future
import akka.http.scaladsl.model.MediaTypes

class MainActor  extends Actor with ActorLogging
{
  implicit val system = context.system
  implicit val materializer = ActorFlowMaterializer()
  implicit val executionContext = system.dispatcher

  val server: HttpExt = Http(context.system)
  var serverSource: Source[IncomingConnection, Future[ServerBinding]] = null

  def routes =
    get {
      pathSingleSlash {
        complete {
          HttpResponse(  entity = HttpEntity(MediaTypes.`text/html`,  Index.template   ))
        }
      } ~
        path("mystyles.css")(complete{
          HttpResponse(  entity = HttpEntity(MediaTypes.`text/css`,  MyStyles.render   ))
        }) ~
      // Scala-JS puts them in the root of the resource directory per default,
      // so that's where we pick them up
      path("frontend-fastopt.js")(getFromResource("frontend-fastopt.js")) ~
      path("frontend-launcher.js")(getFromResource("frontend-launcher.js"))
    }

  override def receive: Receive = {
    case AppMessages.Start(port)=>
      val host = "localhost"
      server.bindAndHandle(routes, host, port)

      //serverSource = server.bind(interface = host, port)
      //serverSource.runForeach {      connection =>   connection.handleWithAsyncHandler(futureHandler)      }
      log.info(s"starting server at $host:$port")


    case AppMessages.Stop=>
      log.info("stopping")

  }

}
