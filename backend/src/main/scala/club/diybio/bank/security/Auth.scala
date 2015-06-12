package club.diybio.bank.security

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.{CookieDirectives, FutureDirectives, SecurityDirectives}

trait Auth {
  self:SecurityDirectives
    with FutureDirectives
    with CookieDirectives=>

  def loginManager:LoginManager


  def logins: Route = //note cookies will be changed to session in future
    path("users" / "login")
    {
      parameter("username","password") { (name,password) ⇒
        onSuccess(loginManager.checkPassword(name,password)) {
          case true  =>
            setCookie(HttpCookie("username", content = name)) {
              complete(s"The user $name was logged in")
            }
          case false =>
            ctx=> ctx.reject(AuthorizationFailedRejection )
        }

      }
    }

}

case class UserAlreadyExists(name:String) extends Rejection