package club.diybio.bank
import akka.http.scaladsl.model.{MediaTypes, _}
import akka.http.scaladsl.server.Directives._
import club.diybio.bank.templates.{Index, MyStyles}

import scalacss.Defaults._

/**
 * Trait that countains routes and handlers
 */
trait Routes {

  lazy val webjarsPrefix = "lib"
  lazy val resourcePrefix = "resources"

  def index =  pathSingleSlash {
    complete { HttpResponse(  entity = HttpEntity(MediaTypes.`text/html`,  Index.template   ))      }
  }

  def mystyles =    path("styles" / "mystyles.css"){
    complete  {
      HttpResponse(  entity = HttpEntity(MediaTypes.`text/css`,  MyStyles.render   ))   }
  }
/*
  def loadScalajs =  path("frontend-fastopt.js")(getFromResource("frontend-fastopt.js")) ~
    path("frontend-launcher.js")(getFromResource("frontend-launcher.js"))*/

  def loadResources = pathPrefix(resourcePrefix~Slash) {
    getFromResourceDirectory("")
  }



  def webjars =pathPrefix(webjarsPrefix ~ Slash)  {  getFromResourceDirectory(webjarsPrefix)  }


  def routes = index ~  webjars ~ mystyles ~ loadResources

}
