package club.diybio.bank.domain.bio

import scala.util.Try

import org.w3.banana._

import club.diybio.bank.domain.bio.PlasmidVector.PlasmidId

class BananaPlasmidVectorRepository[Rdf <: RDF, Connection](connection: Connection)
  (implicit
    val rdfOps: RDFOps[Rdf],
    val sparqlOps: SparqlOps[Rdf],
    val sparqlEngine: SparqlEngine[Rdf, Try, Connection]) extends PlasmidVectorRepository {

  import sparqlEngine.sparqlEngineSyntax._
  import sparqlOps._
  import rdfOps._

  def getById(id: PlasmidId): Option[PlasmidVector] = {

    val query = parseSelect(
      s"""
        PREFIX foaf: <http://xmlns.com/foaf/0.1/>

        SELECT ?name
        WHERE {
          <http://bank.diybio.club/plasmid/$id> foaf:name ?name
        }
      """).get

    connection.executeSelect(query).get.iterator.to[Iterable].map {
      row => PlasmidVector(id, row("name").get.as[String].get)
    }.toSeq.headOption
  }
}
