package club.diybio.bank.domain

sealed trait CloningInfo

case class RestrictionEnzymeCloningInfo(
  fivePrimeSite: Option[RestrictionCloningSite],
  threePrimeSite: Option[RestrictionCloningSite],
  fivePrimeSequencingPrimer: Option[Primer],
  threePrimeSequencingPrimer: Option[Primer]) extends CloningInfo
