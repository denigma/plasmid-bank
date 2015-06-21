package club.diybio.bank.utils.sparql

import org.w3.banana.{RDF, RDFOps}

/** http://www.w3.org/TR/sparql11-update/ */
class SPARQLUpdateSyntax[Rdf<:RDF](implicit val ops:RDFOps[Rdf]) extends QuerySyntax[Rdf]
{
  import ops._
/*


  object SameSubjectTemplate{

    implicit def fromGraph(graph: Rdf#Graph): SameSubjectTemplate = {

    }
  }
  trait SameSubjectTemplate

  object QuadData
  {

    object TriplesTemplate

    implicit def fromNamedGraph(params:(CanBePredicate,Rdf#Graph)): QuadData = new QuadData {

      def stringValue:String = ???
    }

    implicit def fromGraph(graph: Rdf#Graph): QuadData = new QuadData {

      lazy val subjects: Map[Rdf#Node, Seq[Rdf#Triple]] = graph.triples.toSeq.groupBy(trip=>trip.subject.toUri)

      def stringValue:String = sorted.foldLeft("")( (acc,tr)
        acc + tr
      )
    }
  }
  trait QuadData extends QueryElement


  case class
*/

}
