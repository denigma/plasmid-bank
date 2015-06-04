package club.diybio.bank.domain.bio

sealed trait CopyNumber

case object HIGH_NUMBER extends CopyNumber
case object LOW_NUMBER extends CopyNumber
