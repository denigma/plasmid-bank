package club.diybio.bank.routes

import akka.http.scaladsl.server.Directive
import play.twirl.api.Html

trait PJaxMagnet {
  def directive: Directive[Tuple1[Html]]
}

object PJaxMagnet {

  implicit def apply(params:(Html,Html=>Html)):PJaxMagnet =
    new PJaxMagnet {
      def directive = Directive[Tuple1[Html]] { inner ⇒ ctx ⇒
          val (html, transform) = params
          if (ctx.request.headers.exists(h => h.lowercaseName() == "x-pjax"))
            inner(Tuple1(html))(ctx)
          else
            inner(Tuple1(transform(html)))(ctx)
        }
    }

}


trait PJax {

  def pjax(magnet: PJaxMagnet):Directive[Tuple1[Html]] = magnet.directive
}
