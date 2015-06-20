package club.diybio.bank.domain

sealed case class TargetSpecie(label: String) extends Labeled

// We are safe to use inheritance on case class as long as we don't add extra fields and just using case objects as
// convenient tags. However it must be changed to more verbose trait-based approach if extra fields on subclasses will
// be needed for any purpose
object MAMMALIAN extends TargetSpecie("Mammalian")
object BACTERIAL extends TargetSpecie("Bacterial")
object YEAST extends TargetSpecie("Yeast")
object WORM extends TargetSpecie("Worm")
object INSECT extends TargetSpecie("Insect")
object PLANT extends TargetSpecie("Plant")
