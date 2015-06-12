package club.diybio.bank.domain.bank

import scala.util.Try

import org.w3.banana.binder.RecordBinder
import org.w3.banana.{PointedGraph, Prefix, RDF, RDFOps, RDFStore}

class BananaDepositorRepository [Rdf <: RDF, Connection](connection: Connection)
  (implicit
    private[this] val rdfOps: RDFOps[Rdf],
    private[this] val rdfStore: RDFStore[Rdf, Try, Connection],
    private[this] val recordBinder: RecordBinder[Rdf])
  extends DepositorRepository {

  import rdfOps._

    private[this] def rdfId(id: DepositorId): Rdf#URI = makeUri(s"depositor:$id")

    private[this] object binders {

      import recordBinder._

      val depositors = Prefix("depositors", "https://denigma.org/")  // TODO: find better URL. May be some existing schema?

      val state = property[String](depositors("state"))
      val city = property[String](depositors("city"))
      implicit val locationBinder = pgb[Location](state, city)(Location.apply, Location.unapply)

      val id = property[DepositorId](depositors("depositor_id"))
      val name = property[String](depositors("name"))
      val email = property[String](depositors("email"))
      val location = property[Location](depositors("location"))

      implicit val depositorBinder = pgbWithId[Depositor](d => rdfId(d.id))(id, name,
        email, location)(Depositor.apply, Depositor.unapply)
    }

  import binders.depositorBinder

  override def getById(id: DepositorId): Option[Depositor] = {
    val uri = rdfId(id)
    val g = rdfStore.getGraph(connection, uri).get
    if (g.size == 0) {
      None
    } else {
      val v = PointedGraph(uri, g).as[Depositor]
      Some(v.get)
    }
  }

  override def delete(id: DepositorId) {
    rdfStore.removeGraph(connection, rdfId(id))
  }

  override def upsert(depositor: Depositor): Unit = {
    delete(depositor.id)  // this is for upsert semantics. Probably must be surrounded with transaction

    val uri = rdfId(depositor.id)
    val pg = depositor.toPointedGraph
    val result = rdfStore.appendToGraph(connection, uri, pg.graph)
    result.get  // just to throw an exception for Failure
  }
}
