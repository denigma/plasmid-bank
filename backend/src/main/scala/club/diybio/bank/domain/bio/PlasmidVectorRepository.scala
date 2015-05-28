package club.diybio.bank.domain.bio

trait PlasmidVectorRepository {
  def getById(id: PlasmidVector.PlasmidId): Option[PlasmidVector]
}
