package club.diybio.bank.domain.bio

case class BacteriaGrowthInfo(
  resistance: Set[Resistance],
  temperature: Double,
  strains: Set[Strain],
  copyNumber: CopyNumber)
