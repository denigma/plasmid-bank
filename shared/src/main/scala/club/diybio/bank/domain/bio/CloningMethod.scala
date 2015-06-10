package club.diybio.bank.domain.bio

import club.diybio.bank.domain.Labeled

sealed case class CloningMethod(label: String) extends Labeled

// We are safe to use inheritance on case class as long as we don't add extra fields and just using case objects as
// convenient tags. However it must be changed to more verbose trait-based approach if extra fields on subclassess will
// be needed for any purpose
object RESTRICTION_ENZYME extends CloningMethod("Restriction enzyme")
object PCR extends CloningMethod("PCR")
object ANNEALED_OLIGO extends CloningMethod("Annealed oligo")
object GIBSON_ASSEMBLY extends CloningMethod("Gibson assembly")
