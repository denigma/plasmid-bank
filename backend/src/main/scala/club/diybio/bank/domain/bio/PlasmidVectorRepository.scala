package club.diybio.bank.domain.bio

trait PlasmidVectorRepository {
  def getById(id: PlasmidVectorId): Option[PlasmidVector]

  def upsert(plasmidVector: PlasmidVector)
}
