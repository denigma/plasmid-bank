package club.diybio.bank.domain.bio

case class PlasmidVector(
  id: PlasmidVectorId,
  backbone: Option[PlasmidVectorId],
  comment: Option[String],
  purpose: Option[String],
  size: Integer,
  expressionType: Set[ExpressionType],
  selectableMarker: Set[SelectableMarker],
  growthInfo: BacteriaGrowthInfo,
  sequenceInfo: SequenceInfo,
  cloningInfo: Option[CloningInfo],
  insert: Option[PlasmidInsert])
