package club.diybio.bank.domain.bank

import club.diybio.bank.domain.{PlasmidVector, TargetSpecie, PlasmidVectorId}

sealed trait DepositedItemSpec {
  def satisfiedBy(vector: PlasmidVector): Boolean
  
//  def &(other: DepositedItemSpec): DepositedItemSpec = ConjunctDepositedItemSpec(Seq(this, other))
//  def |(other: DepositedItemSpec): DepositedItemSpec = DisjunctDepositedItemSpec(Seq(this, other))
}

case class SimplePlasmidVectorSpecification (
  id: Option[PlasmidVectorId],
  expressionType: Option[Set[TargetSpecie]]
) extends DepositedItemSpec {

  override def satisfiedBy(vector: PlasmidVector) = Seq(
    id.map(_ == vector.id),
    expressionType.map(_.subsetOf(vector.expressionTypes))).forall(_.contains(true))
}

// TODO: we may need them later
//case class ConjunctDepositedItemSpec (
//  specs: Seq[DepositedItemSpec]
//) extends DepositedItemSpec {
//
//  override def satisfiedBy(vector: PlasmidVector) = specs.forall(_.satisfiedBy(vector))
//}
//
//case class DisjunctDepositedItemSpec (
//  specs: Seq[DepositedItemSpec]
//) extends DepositedItemSpec {
//  override def satisfiedBy(vector: PlasmidVector) = specs.exists(_.satisfiedBy(vector))
//}
