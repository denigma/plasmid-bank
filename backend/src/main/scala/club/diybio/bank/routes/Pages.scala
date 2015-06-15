package club.diybio.bank.routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import play.twirl.api.Html

class Pages extends Directives with PJax{

  def defaultPage:Option[Html] = {
    None
  }

  def index =  pathSingleSlash{ctx=>
    ctx.flowMaterializer.executionContext
    ctx.complete {
      HttpResponse(  entity = HttpEntity(MediaTypes.`text/html`, html.index(defaultPage).body  ))
    }
  }

  val loadPage:Html=>Html = h=>html.index(Some(h))


  def test = pathPrefix("test"~Slash) { ctx=>
      pjax(Html(s"<h1>${ctx.unmatchedPath}</h1>"),loadPage){h=>c=>
        val resp = HttpResponse(  entity = HttpEntity(MediaTypes.`text/html`, h.body  ))
        c.complete(resp)
      }(ctx)
    }



  def routes = index ~ test


}