package club.diybio.bank


import akka.http.scaladsl.model.{MediaTypes, _}
import akka.http.scaladsl.server.Directives._
import club.diybio.bank.templates.{Index, MyStyles}

import scalacss.Defaults._

/**
 * Trait that countains routes and handlers
 */
trait Routes extends Auth with PJax{

  lazy val webjarsPrefix = "lib"

  lazy val resourcePrefix = "resources"

  def index =  pathSingleSlash {
    complete { HttpResponse(  entity = HttpEntity(MediaTypes.`text/html`,  Index.template   ))      }
  }

  def mystyles =    path("styles" / "mystyles.css"){
    complete  {
      HttpResponse(  entity = HttpEntity(MediaTypes.`text/css`,  MyStyles.render   ))   }
  }

  def loadResources = pathPrefix(resourcePrefix~Slash) {
    getFromResourceDirectory("")
  }

  def webjars =pathPrefix(webjarsPrefix ~ Slash)  {  getFromResourceDirectory(webjarsPrefix)  }

  def routes = index ~  webjars ~ mystyles ~ loadResources

}

//here will be pjax implementation in some time
trait PJax {

  def isPjax(req:HttpRequest) = req.headers.exists(h=>h.lowercaseName()=="x-pjax")

}