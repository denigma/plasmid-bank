object Versions extends WebJarsVersions with ScalaJSVersions with RdfVersions with SharedVersions
{
	val scala = "2.11.6"

	val akkaHttp = "1.0-RC3"

	val ammonite = "0.3.2"

	val akkaHttpExtensions = "0.0.2"

	val scalatest = "3.0.0-M3"

}

trait ScalaJSVersions {

	val jqueryFacade = "0.6"

	val jsext = "0.5"

	val dom = "0.8.1"

	val binding = "0.7.16"

	val bindingControls = "0.0.2"

}

//versions for libs that are shared between client and server
trait SharedVersions
{
	val autowire = "0.2.5"

	val scalaRx = "0.2.8"

	val quicklens = "1.3.1"

	val scalaTags = "0.5.2"

	val scalaCSS = "0.3.0"

	val productCollections = "1.4.2"

	val utest = "0.3.1"
}

trait WebJarsVersions {

	val jquery =  "2.1.3"

	val semanticUI = "1.12.3"

	val selectize = "0.12.1"
}

trait RdfVersions {

  val bananaRdf = "0.8.1"

  val sesame = "2.8.3"

	val bananaBigdata = "0.8.2-SNAP3"

	val bigdataVersion = "1.5.1"

	val bigdataSesameVersion = "2.7.13"

	val bigdataLuceneVersion = "3.0.0"
}
