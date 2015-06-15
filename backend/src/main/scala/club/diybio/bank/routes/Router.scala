package club.diybio.bank.routes

import akka.http.scaladsl.server.Directives
import club.diybio.bank.security.{InMemoryLoginController, LoginController, InMemorySessionController, SessionController}


class Router extends Directives {
  val sessionController:SessionController = new InMemorySessionController
  val loginController:LoginController = new InMemoryLoginController

  def routes = new Head().routes ~
    new Registration(
      loginController.checkPassword,
      sessionController.withToken)
      .routes ~
    new Pages().routes

}
