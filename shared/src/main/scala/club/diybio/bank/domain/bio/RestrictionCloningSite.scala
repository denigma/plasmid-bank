package club.diybio.bank.domain.bio

case class RestrictionCloningSite(
  site: DNASeq,
  destroyed: Option[Boolean])
