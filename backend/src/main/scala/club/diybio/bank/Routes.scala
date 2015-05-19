package club.diybio.bank
import akka.actor._
import akka.http.scaladsl.Http.{ServerBinding, IncomingConnection}

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl._

import akka.routing._
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Source
import HttpMethods._
import club.diybio.bank.templates.{MyStyles, Index}
import scalacss.Defaults._
import scala.concurrent.Future
import akka.http.scaladsl.model.MediaTypes

/**
 * Trait that countains routes and handlers
 */
trait Routes {

  lazy val webjarsPrefix = "lib"

  def index =  pathSingleSlash {
    complete { HttpResponse(  entity = HttpEntity(MediaTypes.`text/html`,  Index.template   ))      }
  }

  def mystyles =    path("styles" / "mystyles.css"){
    complete  {
      HttpResponse(  entity = HttpEntity(MediaTypes.`text/css`,  MyStyles.render   ))   }
  }

  def loadScalajs =  path("frontend-fastopt.js")(getFromResource("frontend-fastopt.js")) ~
    path("frontend-launcher.js")(getFromResource("frontend-launcher.js"))


  def webjars =pathPrefix(webjarsPrefix ~ Slash)  { where=>
      val dep = webjarsPrefix + "/" + where.unmatchedPath //webjar dependency string path
      //println(dep)
      val resource = getFromResource(dep)
      resource(where)
  }


  def routes = index ~  webjars ~ mystyles ~ loadScalajs

}
