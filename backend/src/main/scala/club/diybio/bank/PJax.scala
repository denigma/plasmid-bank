package club.diybio.bank

import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import play.twirl.api.Html

trait PJax {

  def isPjax(req:HttpRequest) = req.headers.exists(h=>h.lowercaseName()=="x-pjax")

  def loadPage(content:Html)(implicit req:HttpRequest): HttpResponse //should load page in a pjax way

}
