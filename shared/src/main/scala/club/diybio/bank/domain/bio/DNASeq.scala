package club.diybio.bank.domain.bio

case class DNASeq(seq: String, features: Set[Feature] = Set.empty)
