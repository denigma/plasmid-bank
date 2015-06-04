package club.diybio.bank.domain.bio

sealed trait CloningMethod

case object RESTRICTION_ENZYME extends CloningMethod
case object PCR extends CloningMethod
case object ANNEALED_OLIGO extends CloningMethod
case object GIBSON_ASSEMBLY extends CloningMethod
