package club.diybio.bank.domain.bio

case class RestrictionCloningSite(
  site: DNASequence,
  destroyed: Option[Boolean])
