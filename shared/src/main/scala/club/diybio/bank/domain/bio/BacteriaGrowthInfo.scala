package club.diybio.bank.domain.bio

case class BacteriaGrowthInfo(
  resistance: Set[Resistance],
  temperature: Float,
  strains: Set[Strain],
  copyNumber: CopyNumber)
