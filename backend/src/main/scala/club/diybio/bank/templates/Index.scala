package club.diybio.bank.templates
import scalacss.Defaults._
import scalacss.ScalatagsCss._
import scalatags.Text._
import scalatags.Text.all._

object Index {

  lazy val items = for(e <- 0 until 5) yield a(`class` :="item",i(`class`:="lab icon"),s"Item #$e") //some test data

  lazy val HEAD =  head(
    title := "Plasmids bank",
    link(rel := "stylesheet", href := "styles/mystyles.css"), //my scalacss styles
    script(`type` := "text/javascript", src := "lib/jquery/jquery.min.js"),
    link(rel := "stylesheet", href := "lib/selectize.js/css/selectize.css"),
    link(rel := "stylesheet", href := "lib/selectize.js/css/selectize.default.css"),
    script(`type` := "text/javascript", src := "lib/selectize.js/js/standalone/selectize.js"), //for nice select boxes
    link(rel := "stylesheet", href := "lib/Semantic-UI/semantic.css"),
    script(`type` := "text/javascript", src := "lib/Semantic-UI/semantic.js") //http://semantic-ui.com/ is nice alternative to bootstrap
    )

  lazy val MENU = div(`class` := "ui labeled green icon menu",items  )
  lazy val MAIN = div(`class` := "ui green segment",h1("Hello World!"),
    p(id:="hello",`class`:="desc","here will be hello world!")
  )

  lazy val scripts = Seq(
    script(src:="resources/frontend-fastopt.js"),
    script(src:="resources/frontend-launcher.js")
  )

  lazy val BODY = body( MENU, MAIN, div(scripts )
  )

  lazy val HTML = html(HEAD,BODY)


  lazy val template = HTML.render


}
