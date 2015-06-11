package club.diybio.bank.security

import scala.concurrent.Future
import scala.concurrent.Future
import com.github.t3hnar.bcrypt._
import scala.concurrent.ExecutionContext.Implicits.global


trait LoginManager {

  def findHash(username:String):Future[Option[String]]

  def checkPassword(user: String, passw: String): Future[Boolean] =   findHash(user) map {
    case Some(hash)=> passw.isBcrypted(hash)
    case None =>false
  }
}
