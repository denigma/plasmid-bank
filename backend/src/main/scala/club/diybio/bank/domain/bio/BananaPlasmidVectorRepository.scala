package club.diybio.bank.domain.bio

import scala.util.{Failure, Success, Try}

import club.diybio.bank.domain.Labeled
import org.w3.banana.binder.{FromLiteral, ToLiteral, PGBinder, RecordBinder}
import org.w3.banana.{PointedGraph, RDFStore, FailedConversion, XSDPrefix, Prefix, RDFOps, RDF}

class BananaPlasmidVectorRepository[Rdf <: RDF, Connection](connection: Connection)
  (implicit
    private[this] val rdfOps: RDFOps[Rdf],
    private[this] val rdfStore: RDFStore[Rdf, Try, Connection],
    private[this] val recordBinder: RecordBinder[Rdf])
  extends PlasmidVectorRepository {

  import rdfOps._

  private[this] def rdfId(id: PlasmidVectorId): Rdf#URI = makeUri(s"https://denigma.org/$id")   // TODO: find better URL

  private[this] object binders {

    import recordBinder._

    val vectors = Prefix("vectors", "https://denigma.org/")  // TODO: find better URL
    val xsd = XSDPrefix(rdfOps)

    class LabeledToLiteral[T <: Labeled] extends ToLiteral[Rdf, T] {
      override def toLiteral(t: T): Rdf#Literal = Literal(t.label, xsd.string)
    }
    class LabeledFromLiteral[T](builder: String => T) extends FromLiteral[Rdf, T] {
      override def fromLiteral(literal: Rdf#Literal): Try[T] = {
        val Literal(lexicalForm, datatype, _) = literal
        if (datatype == xsd.string || datatype == null)   // some RDF stores may return null for datatype
          Success(builder(lexicalForm))
        else
          Failure(FailedConversion(s"$literal is not an xsd:string"))
      }
    }

    implicit val resistanceToLiteral = new LabeledToLiteral[Resistance]
    implicit val literalToResistance = new LabeledFromLiteral[Resistance](Resistance)
    val resistanceBinder = implicitly[PGBinder[Rdf, Resistance]]

    implicit val strainToLiteral = new LabeledToLiteral[Strain]
    implicit val literalToStrain = new LabeledFromLiteral[Strain](Strain)
    val strainBinder = implicitly[PGBinder[Rdf, Strain]]

    implicit val featureToLiteral = new LabeledToLiteral[Feature]
    implicit val literalToFeature = new LabeledFromLiteral[Feature](Feature)
    val featureBinder = implicitly[PGBinder[Rdf, Feature]]

    implicit val copyNumberToLiteral = new LabeledToLiteral[CopyNumber]
    implicit val literalToCopyNumber = new LabeledFromLiteral[CopyNumber](CopyNumber)
    val copyNumberBinder = implicitly[PGBinder[Rdf, CopyNumber]]

    implicit val promoterToLiteral = new LabeledToLiteral[Promoter]
    implicit val literalToPromoter = new LabeledFromLiteral[Promoter](Promoter)
    val promoterBinder = implicitly[PGBinder[Rdf, Promoter]]

    implicit val targetSpecieToLiteral = new LabeledToLiteral[TargetSpecie]
    implicit val literalToTargetSpecie = new LabeledFromLiteral[TargetSpecie](TargetSpecie)
    val targetSpecieBinder = implicitly[PGBinder[Rdf, TargetSpecie]]

    implicit val selectableMarkerToLiteral = new LabeledToLiteral[SelectableMarker]
    implicit val literalToSelectableMarker = new LabeledFromLiteral[SelectableMarker](SelectableMarker)
    val selectableMarkerBinder = implicitly[PGBinder[Rdf, SelectableMarker]]

    val resistance = set[Resistance](vectors("resistance"))
    val temperature = property[Double](vectors("growth_temperature"))
    val strains = set[Strain](vectors("bacteria_strain"))
    val copyNumber = property[CopyNumber](vectors("copy_number"))
    implicit val bacteriaGrowthInfoBinder = pgb[BacteriaGrowthInfo](resistance, temperature,
      strains, copyNumber)(BacteriaGrowthInfo.apply, BacteriaGrowthInfo.unapply)

    val seq = property[String](vectors("sequence"))
    val features = set[Feature](vectors("feature"))
    implicit val DNASeqBinder = pgb[DNASeq](seq, features)(DNASeq.apply, DNASeq.unapply)

    val fullSeq = optional[DNASeq](vectors("full_sequence"))
    val partialSeqs = set[DNASeq](vectors("partial_sequence"))
    implicit val sequenceInfoBinder = pgb[SequenceInfo](fullSeq, partialSeqs)(SequenceInfo.apply, SequenceInfo.unapply)

    val label = property[String](vectors("name"))
    val primerSeq = property[DNASeq](vectors("primer_sequence"))
    implicit val primerBinder = pgb[Primer](label, primerSeq)(Primer.apply, Primer.unapply)

    val restrictionSiteSeq = property[DNASeq](vectors("restriction_site_seq"))
    val destroyed = optional[Boolean](vectors("destroyed"))
    implicit val restrictionCloningSiteBinder = pgb[RestrictionCloningSite](restrictionSiteSeq,
      destroyed)(RestrictionCloningSite.apply, RestrictionCloningSite.unapply)

    val fivePrimeSite = optional[RestrictionCloningSite](vectors("five_prime_site"))
    val threePrimeSite = optional[RestrictionCloningSite](vectors("three_prime_site"))
    val fivePrimePrimer = optional[Primer](vectors("five_prime_primer"))
    val threePrimePrimer = optional[Primer](vectors("three_prime_primer"))
    implicit val cloningInfoBinder = pgb[RestrictionEnzymeCloningInfo](fivePrimeSite, threePrimeSite,
      fivePrimePrimer, threePrimePrimer)(RestrictionEnzymeCloningInfo.apply, RestrictionEnzymeCloningInfo.unapply)

    val altNames = set[String](vectors("alternative_name"))
    val insertSize = property[Int](vectors("insert_size"))
    val species = set[String](vectors("specie"))
    val promoter = optional[Promoter](vectors("promoter"))
    val genBankId = optional[String](vectors("gen_bank_id"))
    implicit val plasmidInsertBinder = pgb[PlasmidInsert](label, altNames, species, insertSize,
      promoter, genBankId)(PlasmidInsert.apply, PlasmidInsert.unapply)

    val id = property[String](vectors("vector_id"))
    val backboneId = optional[String](vectors("backbone_id"))
    val comment = optional[String](vectors("comment"))
    val vectorSize = property[Int](vectors("vector_size"))
    val expressionType = set[TargetSpecie](vectors("target_specie"))
    val markers = set[SelectableMarker](vectors("selectable_marker"))
    val growthInfo = property[BacteriaGrowthInfo](vectors("growth_info"))
    val sequenceInfo = property[SequenceInfo](vectors("sequence_info"))
    val cloningInfo = optional[RestrictionEnzymeCloningInfo](vectors("cloning_info"))
    val plasmidInsert = optional[PlasmidInsert](vectors("gene_insert"))
    implicit val plasmidVectorBinder = pgbWithId[PlasmidVector](v => rdfId(v.id))(id, backboneId, comment, vectorSize,
      expressionType, markers, growthInfo, sequenceInfo, cloningInfo,
      plasmidInsert)(PlasmidVector.apply, PlasmidVector.unapply)
  }

  import binders.plasmidVectorBinder

  def getById(id: PlasmidVectorId): Option[PlasmidVector] = {
    val uri = rdfId(id)
    val g = rdfStore.getGraph(connection, uri).get
    if (g.size == 0) {
      None
    } else {
      val v = PointedGraph(uri, g).as[PlasmidVector]
      Some(v.get)
    }
  }

  def delete(id: PlasmidVectorId) {
    rdfStore.removeGraph(connection, rdfId(id))
  }

  def upsert(plasmidVector: PlasmidVector) {
    delete(plasmidVector.id)  // this is for upsert semantics. Probably must be surrounded with transaction

    val uri = rdfId(plasmidVector.id)
    val pg = plasmidVector.toPointedGraph
    val result = rdfStore.appendToGraph(connection, uri, pg.graph)
    result.get  // just to throw an exception for Failure
  }
}
