package club.diybio.bank.security

import akka.http.scaladsl.server.{Directive, Directive0, MissingQueryParamRejection}
import akka.http.scaladsl.util.FastFuture._

import scala.concurrent.Future
import scala.util._

trait RegisterMagnet {
  def directive: Directive0
}

object RegisterMagnet {

  type FutureRegistration = (String,String)=>Future[Boolean]
  type TryRegistration = (String,String)=>Try[Boolean]

  /**
   * To convert Unit directive into magnet
   * @param dir note it is used mostly for applies of this magnet
   * @return
   */
  implicit private[this] def directive2magnet(dir:Directive0 ):RegisterMagnet =
    new RegisterMagnet {
      val directive:Directive0 = dir
    }

  implicit def tryRegistrationDefault(register: TryRegistration):RegisterMagnet =
    Directive[Unit]{ inner ⇒ ctx ⇒
      val q = ctx.request.uri.query
      (q.get("username"), q.get("password")) match
      {

        case (Some(username),Some(password))=>
          register(username, password) match {
            case Success(true) => inner(Unit)(ctx)
            case Success(false) => ctx.reject(UserAlreadyExists(username))
            case Failure(th) => ctx.reject(ReadErrorRejection(s"cannot register $username", th))
          }
        case (None,Some(p)) =>  ctx.reject(MissingQueryParamRejection("username") )
        case (Some(u),None) =>  ctx.reject(MissingQueryParamRejection("password") )
        case (None,None) =>  ctx.reject(MissingQueryParamRejection("username") , MissingQueryParamRejection("password"))
      }
    }

  implicit def tryRegistration(params:(String,String, TryRegistration)):RegisterMagnet =
      Directive[Unit]{ inner ⇒ ctx ⇒
        val (username,password,register) = params
        register(username,password) match {
          case Success(true) =>inner(Unit)(ctx)
          case Success(false) =>  ctx.reject(UserAlreadyExists(username))
          case Failure(th) =>ctx.reject(ReadErrorRejection(s"cannot register $username",th))
        }
      }

  implicit def futureRegistration(params:(String,String, FutureRegistration)):RegisterMagnet =
      Directive[Unit] { inner ⇒ ctx ⇒
        import ctx.executionContext
        val (username,password,register) = params
        register(username,password).fast
          .flatMap{
          case false =>  ctx.reject(UserAlreadyExists(username))
          case true =>inner(Unit)(ctx)
          }
          .recoverWith{
            case th=>  ctx.reject(ReadErrorRejection(s"cannot register  $username",th))
          }
      }

}