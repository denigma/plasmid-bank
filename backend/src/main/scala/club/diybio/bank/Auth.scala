package club.diybio.bank

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers._

trait Auth {

  lazy val authorization =Authorization(BasicHttpCredentials("user", "pass"))

  // a method that extracts basic HTTP credentials from a request
  def credentialsOfRequest(req: HttpRequest): Option[User] =
    for {
      Authorization(BasicHttpCredentials(user, pass)) <- req.header[Authorization]
    } yield User(user, pass)

}

case class User(user:String,pass:String)