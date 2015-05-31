package club.diybio.bank


import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import club.diybio.bank.templates.{Index, MyStyles}
import play.twirl.api.Html

import scalacss.Defaults._

/**
 * Trait that countains routes and handlers
 */
trait Routes extends Auth with PJax{

  lazy val webjarsPrefix = "lib"

  lazy val resourcePrefix = "resources"

  def index =  pathSingleSlash{ctx=>
    ctx.complete {
      HttpResponse(  entity = HttpEntity(MediaTypes.`text/html`, html.index(None).body  ))
    }
  }

  def mystyles =    path("styles" / "mystyles.css"){
    complete  {
      HttpResponse(  entity = HttpEntity(MediaTypes.`text/css`,  MyStyles.render   ))   }
  }

  def loadResources = pathPrefix(resourcePrefix~Slash) {
    getFromResourceDirectory("")
  }

  def webjars =pathPrefix(webjarsPrefix ~ Slash)  {  getFromResourceDirectory(webjarsPrefix)  }

  def test = pathPrefix("test"~Slash) { ctx=>
    ctx.complete{
      val txt = s"<h1>${ctx.unmatchedPath}</h1>"
      this.loadPage(Html(txt))(ctx.request)
    }
  }


  /**
   * loads page into index. It almost works, the only problem is that I have to change relative pathes in my templates
   * @param content
   * @param req
   * @return
   */
  def loadPage(content:Html)(implicit req:HttpRequest): HttpResponse = {
    val cont = if(isPjax(req))  content.body else html.index(  Some(content ) ).body
    HttpResponse(  entity = HttpEntity(MediaTypes.`text/html`, cont))
  }

  def routes = index ~  webjars ~ mystyles ~ test ~ loadResources
}

trait PJax {

  def isPjax(req:HttpRequest) = req.headers.exists(h=>h.lowercaseName()=="x-pjax")

  def loadPage(content:Html)(implicit req:HttpRequest): HttpResponse //should load page in a pjax way

}