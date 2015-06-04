package club.diybio.bank.domain.bio

sealed trait CloningInfo

case class RestrictionEnzymeCloningInfo(
  fivePrimeSite: Option[RestrictionCloningSite],
  threePrimeSite: Option[RestrictionCloningSite],
  fivePrimeSequencingPrimer: Option[DNASequence],
  threePrimeSequencingPrimer: Option[DNASequence]) extends CloningInfo
