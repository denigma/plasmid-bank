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

/**
 * Main actor that encapsulates main application logic and starts the server
 */
class MainActor  extends Actor with ActorLogging with Routes
{
  implicit val system = context.system
  implicit val materializer = ActorFlowMaterializer()
  implicit val executionContext = system.dispatcher

  val server: HttpExt = Http(context.system)

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
