import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._


object Dependencies {

	//libs for testing
  lazy val testing = Def.setting(Seq(
    "com.lihaoyi" %%% "utest" % Versions.utest % "test",

    "org.scalatest" %%% "scalatest-all" % Versions.scalaTest % "test"
  ))

	//akka-related libs
	lazy val akka = Def.setting(Seq(

		"com.typesafe.akka" %% "akka-stream-experimental" % Versions.akkaHttp,

		"com.typesafe.akka" %% "akka-http-core-experimental" % Versions.akkaHttp,

		"com.typesafe.akka" %% "akka-http-experimental" % Versions.akkaHttp,

		"com.typesafe.akka" %% "akka-http-testkit-experimental" % Versions.akkaHttp % "test"


	))

	lazy val templates = Def.setting(Seq(
		"com.github.japgolly.scalacss" %%% "core" % Versions.scalaCSS,

		"com.github.japgolly.scalacss" %%% "ext-scalatags" %  Versions.scalaCSS
	))
  
	//scalajs libs
	lazy val sjsLibs= Def.setting(Seq(
		"org.scala-js" %%% "scalajs-dom" % Versions.dom,

		"org.querki" %%% "jquery-facade" % Versions.jqueryFacade, //scalajs facade for jQuery + jQuery extensions

		"org.querki" %%% "querki-jsext" % Versions.jsext, //useful sclalajs extensions

		"org.denigma" %%% "binding" % Versions.binding
	))

	//dependencies on javascript libs
	lazy val webjars= Def.setting(Seq(

		"org.webjars" % "Semantic-UI" % Versions.semanticUI, //css theme, similar to bootstrap

		"org.webjars" % "selectize.js" % Versions.selectize //select control
	))

	//common purpose libs
	lazy val commonShared = Def.setting(Seq(
		"com.softwaremill.quicklens" %%% "quicklens" % Versions.quicklens//, //nice lenses for case classes
	))

	lazy val rdf = Def.setting(Seq(
    		"org.w3" %% "banana-sesame" % Versions.bananaRdf
  ))

	val otherJvm = Def.setting(Seq(
		"me.lessis" %% "retry" % Versions.retry,

		"com.github.t3hnar" %% "scala-bcrypt" % Versions.bcrypt, //for password hashes

		"commons-codec" % "commons-codec" % Versions.apacheCodec //for base64 encoding
	))

}
