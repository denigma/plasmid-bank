package club.diybio.bank.routes

import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.server._
import club.diybio.bank.security.AuthDirectives

import scala.concurrent.Future

class Registration(register:(String,String)=>Future[Boolean],getToken:String=>Future[String]) extends AuthDirectives with Directives
{

  def routes: Route =
    path("users" / "login")
    {
      parameter("username","password")
      {
        (username,password) ⇒ withRegistration(username,password,register) {
          withSession(username,getToken){ token=>
            setCookie(HttpCookie("token", content = token)) {
              complete(s"The user $username was logged in")
            }
          }
        }
      }
    }
}

