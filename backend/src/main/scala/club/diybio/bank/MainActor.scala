package club.diybio.bank

import akka.actor._
import akka.http.scaladsl.{Http, _}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorFlowMaterializer
import club.diybio.bank.security.UserAlreadyExists

/**
 * Main actor that encapsulates main application logic and starts the server
 */
class MainActor  extends Actor with ActorLogging
{
  implicit val system = context.system
  implicit val materializer = ActorFlowMaterializer()
  implicit val executionContext = system.dispatcher

  val router = new Router()

  implicit def rejectionHandlers =
    RejectionHandler.newBuilder()
      .handle { case MissingCookieRejection(cookieName) =>
      complete(HttpResponse(BadRequest, entity = "No cookies, no service!!!"))
    }
      .handle { case AuthorizationFailedRejection ⇒
      complete(Forbidden, "You’re out of your depth!")
    }
      .handleAll[MethodRejection] { methodRejections ⇒
      val names = methodRejections.map(_.supported.name)
      complete(MethodNotAllowed, s"Can’t do that! Supported: ${names mkString " or "}!")
    }
      .handleAll[UserAlreadyExists]{ rjs=>
      complete("user no found exception")
    }
      .result()


  val server: HttpExt = Http(context.system)

  override def receive: Receive = {
    case AppMessages.Start(config)=>
      val (host,port) = (config.getString("app.host") , config.getInt("app.port"))
      server.bindAndHandle(router.routes, host, port)

    case AppMessages.Stop=>  onStop()
  }

  def onStop() = {
    log.info("Main actor has been stoped...")
  }

  override def postStop() = {
    onStop()
  }


}
