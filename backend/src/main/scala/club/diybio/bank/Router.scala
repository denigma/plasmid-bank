package club.diybio.bank


import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import club.diybio.bank.security.{Auth, InMemoryLogin, LoginManager}
import club.diybio.bank.templates.MyStyles
import play.twirl.api.Html

import scalacss.Defaults._

class Router extends Auth with PJax with Directives{

  lazy val webjarsPrefix = "lib"

  lazy val resourcePrefix = "resources"

  val loginManager:LoginManager = new InMemoryLogin //temporal solution until we will add user repository

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

  def routes = index ~  webjars ~ mystyles ~ logins ~ test ~ loadResources
}

