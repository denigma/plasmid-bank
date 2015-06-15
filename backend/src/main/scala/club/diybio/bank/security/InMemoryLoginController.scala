package club.diybio.bank.security

import javax.management.openmbean.KeyAlreadyExistsException

import club.diybio.bank.utils.BiMap
import com.github.t3hnar.bcrypt._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

case class User(username:String,hash:String) //TODO: change in the future

class InMemoryLoginController extends LoginController {

  var users:Map[String,User] = Map.empty

  def register(username:String,passw:String):Try[String] = users.get(username) match {
    case Some(user)=>
      Failure(new KeyAlreadyExistsException(username))
    case None=>
      val hash = passw.bcrypt
      users = users + (username -> User(username, hash))
      Success(hash)
  }

  def findHash(username: String): Future[Option[String]]  = Future.successful(users.get(username).map(_.hash))

  def clean() =  users = BiMap.empty //for testing
}