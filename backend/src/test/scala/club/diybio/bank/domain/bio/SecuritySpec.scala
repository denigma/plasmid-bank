package club.diybio.bank.domain.bio


import akka.http.scaladsl.model.headers.{HttpCookie, `Set-Cookie`}
import akka.http.scaladsl.server._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import club.diybio.bank.security.{Auth, AES, InMemoryLogin}
import com.github.t3hnar.bcrypt._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util._
import javax.management.openmbean.KeyAlreadyExistsException

import akka.http.scaladsl.model.headers.{HttpCookie, `Set-Cookie`}
import akka.http.scaladsl.server._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.github.t3hnar.bcrypt._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util._

class SecuritySpec  extends WordSpec with Matchers with Directives with ScalaFutures with  ScalatestRouteTest
{

  object router extends Auth with Directives{
    val loginManager = new InMemoryLogin
  }



  val timeout = 500 millis
  implicit override val patienceConfig = new PatienceConfig(timeout)

  "security" should {
    "encode passwords with bcrypt" in { //bcrypt is a bit slow but very reliable
      import router.loginManager._
      val (a, s, x) = (register("anton", "pass1"), register("sasha", "pass2"), register("xenia", "pass3"))
      checkPassword("anton", "pass2").futureValue shouldEqual false
      checkPassword("anton", "pass1").futureValue shouldEqual true
      checkPassword("sasha", "pass2").futureValue shouldEqual true
      checkPassword("sasha", "pass3").futureValue shouldEqual false
      checkPassword("karmen", "pass3").futureValue shouldEqual false
      cleanCache()
    }

    "be able to encode/decode with AES" in { //will be used for session signing in Future
      val (one, two, three) = ("one", "two", "three")
      val key = "hello encryption!!!"
      val wrongKey = "I am wrong"

      val (oneEn, twoEn, threeEn) = (AES.encrypt(one, key), AES.encrypt(two, key), AES.encrypt(three, key))
      AES.decrypt(oneEn, key) shouldEqual one
      AES.decrypt(twoEn, key) shouldEqual two
      AES.decrypt(threeEn, key) shouldEqual three
      assert(AES.decrypt(threeEn, key) != one)
      assert(AES.decrypt(threeEn, wrongKey) != three)
    }

    "generate cookies on login" in { //note cookies will be changed to session in the Future
      router.loginManager.register("anton","test")
      Get("/users/login?username=anton&password=test") ~> router.logins ~> check{
        responseAs[String] shouldEqual "The user anton was logged in"
        header[`Set-Cookie`] shouldEqual Some(`Set-Cookie`(HttpCookie("username", content = "anton")))
      }
    }

  }
}