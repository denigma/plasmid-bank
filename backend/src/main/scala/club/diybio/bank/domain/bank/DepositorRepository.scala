package club.diybio.bank.domain.bank

trait DepositorRepository {
  def getById(id: DepositorId): Option[Depositor]

  def delete(id: DepositorId)
  def upsert(depositor: Depositor)
}
