package club.diybio.bank.domain.aux

import org.w3.banana.{RDFOps, RDF, Prefix}

case class RDFPrefixes[Rdf <: RDF](implicit rdfOps: RDFOps[Rdf]) {

  val depositors = Prefix("depositors", "https://denigma.org/")  // TODO: find better URL. May be some existing schema?
  val vectors = Prefix("vectors", "https://denigma.org/")  // TODO: find better URL
}
