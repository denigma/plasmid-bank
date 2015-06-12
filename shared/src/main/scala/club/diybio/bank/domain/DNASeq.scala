package club.diybio.bank.domain

case class DNASeq(seq: String, features: Set[Feature] = Set.empty)
