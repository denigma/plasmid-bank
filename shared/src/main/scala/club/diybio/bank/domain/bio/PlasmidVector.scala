package club.diybio.bank.domain.bio

object PlasmidVector {
  type PlasmidId = String
}

case class PlasmidVector(id: PlasmidVector.PlasmidId, name: String)
