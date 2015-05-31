package club.diybio.bank.views

import club.diybio.bank.views.stubs.WithDomain
import org.denigma.binding.extensions._
import org.denigma.binding.views.BindableView
import org.querki.jquery._
import org.scalajs.dom.raw.HTMLElement
import rx._
/**
 * View for the sitebar
 */
class SidebarView (val elem:HTMLElement,val params:Map[String,Any] = Map.empty[String,Any]) extends BindableView with WithDomain{

  override def activateMacro(): Unit = { extractors.foreach(_.extractEverything(this))}

  val logo = Var("/resources/logo.jpg")

  override def bindElement(el:HTMLElement) = {
    super.bindElement(el)
    $(".ui.accordion").dyn.accordion()
  }

  override protected def attachBinders(): Unit =  binders =  BindableView.defaultBinders(this)
}
