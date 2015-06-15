package club.diybio.bank.security

import com.github.t3hnar.bcrypt._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait LoginController{

  def findHash(user:String):Future[Option[String]]

  def checkPassword(username: String, passw: String): Future[Boolean] =   findHash(username) map {
    case Some(hash)=> passw.isBcrypted(hash)
    case None =>false
  }

}

trait SessionController{

  def withToken(username:String):Future[String]

  def getToken(username:String):Option[String]

}