package club.diybio.bank.domain

case class PlasmidVector(
  id: PlasmidVectorId,
  backbone: Option[PlasmidVectorId],
  comment: Option[String],
  size: Int,
  expressionTypes: Set[TargetSpecie],
  selectableMarker: Set[SelectableMarker],
  growthInfo: BacteriaGrowthInfo,
  sequenceInfo: SequenceInfo,
  cloningInfo: Option[RestrictionEnzymeCloningInfo],
  insert: Option[PlasmidInsert])
