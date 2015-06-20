package club.diybio.bank.domain

case class RestrictionCloningSite(
  site: DNASeq,
  destroyed: Option[Boolean])
