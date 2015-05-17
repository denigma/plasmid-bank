import sbt._
import Keys._

import org.scalajs.sbtplugin.ScalaJSPlugin
import ScalaJSPlugin._
import ScalaJSPlugin.autoImport._
import org.scalajs.sbtplugin.cross.CrossProject

import com.typesafe.sbt.web.SbtWeb
import SbtWeb.autoImport._
import com.typesafe.sbt.web.Import.WebKeys

import bintray._
import BintrayPlugin.autoImport._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

import spray.revolver.RevolverPlugin._

import com.typesafe.sbt.SbtNativePackager
import SbtNativePackager.autoImport._

object Versions extends WebJarsVersions
{
	val scala = "2.11.6"

	val akkaHttp = "1.0-RC2"

	val scalaTags =  "0.5.1"

	val dom = "0.8.0"
	
	val utest = "0.3.1"

	val scalaCSS = "0.2.0"

}

trait WebJarsVersions{

	val jquery =  "2.1.3"

	val semanticUI = "1.11.6"

	val selectize = "0.12.0"
}



object Dependencies {


	lazy val akka = Def.setting(Seq(
			"com.typesafe.akka" %% "akka-http-scala-experimental" % Versions.akkaHttp,

			"com.typesafe.akka" %% "akka-http-testkit-scala-experimental" % Versions.akkaHttp
			))

	lazy val templates = Def.setting(Seq(
		"com.github.japgolly.scalacss" %%% "core" % Versions.scalaCSS,

		"com.github.japgolly.scalacss" %%% "ext-scalatags" %  Versions.scalaCSS
	))

	lazy val sjsLibs= Def.setting(Seq(
		"org.scala-js" %%% "scalajs-dom" % Versions.dom,

		"com.lihaoyi" %%% "utest" % Versions.utest % "test"
	))

	lazy val webjars= Def.setting(Seq(

		"org.webjars" % "jquery" % Versions.jquery,

		"org.webjars" % "Semantic-UI" % Versions.semanticUI,

		"org.webjars" % "selectize.js" % Versions.selectize
	))




}

object Build extends sbt.Build {
  
	//settings for all the projects
	lazy val commonSettings = Seq(
	    scalaVersion := Versions.scala,
	    organization := "club.diybio"
	  )

	//sbt-native-packager settings to run it as daemon
	lazy val packageSettings = Seq(
		maintainer := "Anton Kulaga <antonkulaga@gmail.com>",
		packageSummary:= "Runner for gene set enrichment analysis",
		packageDescription := "Runner for gene set enrichement analysis"
		//serverLoading in Debian := Upstart
	)


	/**
	code shared between backend and frontend
	*/
	lazy val transport = crossProject
	  .crossType(CrossType.Pure)
	  .in(file("transport"))  
	  .settings(commonSettings: _*)
	  .settings(name := "transport")
	lazy val transportJVM = transport.jvm
	lazy val transportJS = transport.js

	// Scala-Js frontend
	lazy val frontend = Project("frontend", file("frontend"))
		.settings(commonSettings: _*)
		.settings(
		persistLauncher in Compile := true,
		persistLauncher in Test := false,
		testFrameworks += new TestFramework("utest.runner.Framework"),
		libraryDependencies ++= Dependencies.sjsLibs.value++Dependencies.templates.value
	) enablePlugins ScalaJSPlugin dependsOn transportJS

	//backend project
	lazy val root = Project("root", file("."))
				.settings(Revolver.settings: _*)
				.settings(commonSettings: _*)
	 			.settings(packageSettings:_*)
		.settings(
			libraryDependencies ++= Dependencies.akka.value++Dependencies.templates.value++Dependencies.webjars.value,
				mainClass in Compile :=Some("club.diybio.bank.Main"),
					resourceGenerators in Compile <+=  (fastOptJS in Compile in frontend, 
				packageScalaJSLauncher in Compile in frontend) map( (f1, f2) => Seq(f1.data, f2.data)),
					watchSources <++= (watchSources in frontend)
				).enablePlugins(SbtWeb) dependsOn transportJVM
		.aggregate(frontend)

  
}
