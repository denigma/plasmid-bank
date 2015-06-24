package club.diybio.bank.utils.sparql

import org.w3.banana.{RDF, RDFOps}

/**
 * Basic syntax shared between all sparql queries
*/
abstract class QuerySyntax[Rdf<:RDF]
{
  def ops:RDFOps[Rdf]

  implicit def fromPattern(params:(CanBeSubject,CanBePredicate,CanBeObject)):Pattern = Pattern(params._1,params._2,params._3)

  case class Pattern(subject:CanBeSubject,predicate:CanBePredicate,objectt:CanBeObject)

  def ?(name:String):Variable = Variable(name)

  case class Variable(name:String) extends QueryElement{
    def stringValue = s"?$name"
  }

  object CanBeSubject{

    implicit def fromBlankNode(node:Rdf#BNode):CanBeSubject = new CanBeSubject {
      def stringValue = node.toString
    }

    implicit def fromURI(node:Rdf#URI):CanBeSubject = new CanBeSubject {
      def stringValue = node.toString
    }

    implicit def fromVar(variable:Variable):CanBeSubject = new CanBeSubject {
      def stringValue = variable.stringValue
    }

  }

  trait CanBeSubject extends QueryElement


  object CanBePredicate{

    implicit def fromURI(node:Rdf#URI):CanBePredicate = new CanBePredicate {
      def stringValue = node.toString
    }

    implicit def fromVar(variable:Variable):CanBePredicate = new CanBePredicate {
      def stringValue = variable.stringValue
    }

  }

  trait CanBePredicate extends QueryElement


  object CanBeObject{

    implicit def fromNode(node:Rdf#Node):CanBeObject = new CanBeObject {
      def stringValue = node.toString
    }

    implicit def fromVar(variable:Variable):CanBeObject = new CanBeObject {
      def stringValue = variable.stringValue
    }

  }

  trait CanBeObject extends QueryElement

  trait QueryElement {
    def stringValue:String //for parsing
  }

}
