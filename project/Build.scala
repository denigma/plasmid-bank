import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.web.SbtWeb
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._
import spray.revolver.RevolverPlugin._
import play.twirl.sbt._
import play.twirl.sbt.SbtTwirl.autoImport._
import com.typesafe.sbt.web.SbtWeb.autoImport._


object Build extends sbt.Build {
  
	//settings for all the projects
	lazy val commonSettings = Seq(
    scalaVersion := Versions.scala,
	  organization := "club.diybio",
		resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases"), //for scala-js-binding
		testFrameworks += new TestFramework("utest.runner.Framework"),
    libraryDependencies ++= Dependencies.shared.value++Dependencies.testing.value,
		updateOptions := updateOptions.value.withCachedResolution(true), //to speed up dependency resolution
		scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature","-language:implicitConversions")
  )

	//sbt-native-packager settings to run it as daemon
	lazy val packageSettings = Seq(
		maintainer := "Anton Kulaga <antonkulaga@gmail.com>",
		packageSummary:= "Plasmid bank",
		packageDescription := "Plasmid bank"
		//serverLoading in Debian := Upstart
	)

	// code shared between backend and frontend
	lazy val shared = crossProject
	  .crossType(CrossType.Pure)
	  .in(file("shared"))
	  .settings(commonSettings: _*)
	  .settings(
			name := "shared"
		)
	.jsSettings( jsDependencies += RuntimeDOM % "test" )
	lazy val sharedJVM = shared.jvm
	lazy val sharedJS = shared.js

	// Scala-Js frontend
	lazy val frontend = Project("frontend", file("frontend"))
		.settings(commonSettings: _*)
		.settings(
			persistLauncher in Compile := true,
			persistLauncher in Test := false,
			jsDependencies += RuntimeDOM % "test",
			testFrameworks += new TestFramework("utest.runner.Framework"),
			libraryDependencies ++= Dependencies.sjsLibs.value
		)	enablePlugins ScalaJSPlugin dependsOn sharedJS aggregate sharedJS

	//backend project
	lazy val backend = Project("backend", file("backend"),settings = commonSettings++Revolver.settings)
		.settings(packageSettings:_*)
		.settings(
			libraryDependencies ++= Dependencies.akka.value++Dependencies.webjars.value++Dependencies.rdf.value,
				mainClass in Compile :=Some("club.diybio.bank.Main"),
        mainClass in Revolver.reStart := Some("club.diybio.bank.Main"),
        resourceGenerators in Compile <+=  (fastOptJS in Compile in frontend,
				  packageScalaJSLauncher in Compile in frontend) map( (f1, f2) => Seq(f1.data, f2.data)),
				resolvers += "Bigdata releases" at "http://systap.com/maven/releases/",
				resolvers += "apache-repo-releases" at "http://repository.apache.org/content/repositories/releases/",
				resolvers += "nxparser-repo" at "http://nxparser.googlecode.com/svn/repository/",
				dependencyOverrides += "org.apache.lucene" % "lucene-core" % Versions.bigdataLuceneVersion, //bigdata uses outdated lucene :_(
				dependencyOverrides += "org.apache.lucene" % "lucene-analyzers" % Versions.bigdataLuceneVersion, //bigdata uses outdated lucene

			watchSources <++= (watchSources in frontend),
      (managedClasspath in Runtime) += (packageBin in Assets).value
		) enablePlugins(SbtTwirl,SbtWeb) dependsOn sharedJVM aggregate sharedJVM

	lazy val root = Project("root",file("."),settings = commonSettings)
		.settings(
			mainClass in Compile := (mainClass in backend in Compile).value
			//libraryDependencies += "com.lihaoyi" % "ammonite-repl_2.11.6" %  Versions.ammonite,
			//initialCommands in console := """ammonite.repl.Repl.run("")""" //better console
    ) dependsOn backend aggregate(backend,frontend)
}
