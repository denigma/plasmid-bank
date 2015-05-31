package club.diybio.bank.domain.bio

import club.diybio.bank.domain.Labeled

sealed case class CopyNumber(label: String) extends Labeled

// We are safe to use inheritance on case class as long as we don't add extra fields and just using case objects as
// convenient tags. However it must be changed to more verbose trait-based approach if extra fields on subclassess will
// be needed for any purpose
object HIGH_NUMBER extends CopyNumber("High copy")
object LOW_NUMBER extends CopyNumber("Low copy")
object VERY_LOW_NUMBER extends CopyNumber("Very low copy")
