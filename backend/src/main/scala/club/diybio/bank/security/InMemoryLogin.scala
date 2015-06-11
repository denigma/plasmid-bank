package club.diybio.bank.security

import javax.management.openmbean.KeyAlreadyExistsException

import com.github.t3hnar.bcrypt._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
 * At the moment - temporal solution,
 * In Future will be used mostly for testing
 */
class InMemoryLogin extends LoginManager {

  var users: Map[String,String] = Map.empty

  def register(username:String,passw:String):Try[String] = users.get(username) match {
    case Some(hash)=>
      Failure(new KeyAlreadyExistsException(username))
    case None=>
      val hash = passw.bcrypt
      users = users + (username -> hash)
      Success(hash)
  }

  def findHash(username: String): Future[Option[String]]  = Future.successful(users.get(username))
}