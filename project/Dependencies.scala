import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._


object Dependencies {

	//libs for testing
  lazy val testing = Def.setting(Seq(
    "com.lihaoyi" %%% "utest" % Versions.utest % "test"
  ))

	//akka-related libs
	lazy val akka = Def.setting(Seq(

		"org.denigma" %%% "akka-http-extensions" % Versions.akkaHttpExtensions,

		"com.typesafe.akka" %% "akka-http-testkit-experimental" % Versions.akkaHttp,

		"org.scalatest" %% "scalatest" % Versions.scalatest % "test" //note in scalajs mode clashes with u-test
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

		"org.denigma" %%% "binding" % Versions.binding,

		"org.denigma" %%% "binding-controls" % Versions.bindingControls
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

		"org.w3" %% "banana-bigdata" % Versions.bananaBigdata excludeAll ExclusionRule(organization = "com.github.inthenow")
	))

}
