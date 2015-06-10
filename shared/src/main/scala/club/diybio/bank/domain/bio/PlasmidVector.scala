package club.diybio.bank.domain.bio

case class PlasmidVector(
  id: PlasmidVectorId,
  backbone: Option[PlasmidVectorId],
  comment: Option[String],
  size: Int,
  expressionType: Set[TargetSpecie],
  selectableMarker: Set[SelectableMarker],
  growthInfo: BacteriaGrowthInfo,
  sequenceInfo: SequenceInfo,
  cloningInfo: Option[RestrictionEnzymeCloningInfo],
  insert: Option[PlasmidInsert])
