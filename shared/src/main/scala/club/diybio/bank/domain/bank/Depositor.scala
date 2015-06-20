package club.diybio.bank.domain.bank

case class Depositor(
  id: DepositorId,
  name: String,
  email: String,
  location: Location)
