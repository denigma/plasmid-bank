package club.diybio.bank.domain.bio

case class PlasmidInsert(
  name: String,
  alternative_names: Seq[String],
  species: Set[String],
  size: Integer,
  genBankId: Option[String]
)
