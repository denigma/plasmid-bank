package club.diybio.bank.domain

trait PlasmidVectorRepository {
   def getById(id: PlasmidVectorId): Option[PlasmidVector]

   def delete(id: PlasmidVectorId)
   def upsert(plasmidVector: PlasmidVector)
 }
