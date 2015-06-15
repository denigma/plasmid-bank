package club.diybio.bank.security

import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives._

import scala.concurrent.Future

/*object Realms
{
  val user = "user area"
  val admin = "admin area"
  val org = "org area"
}*/


trait Auth extends AuthDirectives
{
  self:SecurityDirectives
    with FutureDirectives
    with CookieDirectives
    with BasicDirectives
    with PathDirectives
    with ParameterDirectives =>

  def ses:String=>Future[String]
  def reg:(String,String)=>Future[Boolean]

  def logins: Route =
    path("users" / "login")
    {
      parameter("username","password")
      {
        (username,password) ⇒ withRegistration(username,password,reg) {
          withSession(username,ses){ token=>
            setCookie(HttpCookie("token", content = token)) {
              complete(s"The user $username was logged in")
            }
          }
        }
      }
    }
}

