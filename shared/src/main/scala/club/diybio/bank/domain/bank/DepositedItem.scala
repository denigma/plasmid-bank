package club.diybio.bank.domain.bank

import club.diybio.bank.domain.PlasmidVector

case class DepositedItem(
  depositor: Depositor,
  item: PlasmidVector)
