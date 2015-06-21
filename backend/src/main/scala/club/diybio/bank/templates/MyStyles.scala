package club.diybio.bank.templates

import scala.language.postfixOps

import scalacss.Defaults._

object MyStyles extends StyleSheet.Standalone {
  import dsl._

  "p.desc" - (
    margin(12 px, auto),
    textAlign.left,
    cursor.pointer,
    color.green,

    &.hover -
      cursor.zoomIn,

    &("span") -
      color.red
    )

}
