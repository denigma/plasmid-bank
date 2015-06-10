package club.diybio.bank.domain.bio

import org.openrdf.repository.RepositoryConnection
import org.openrdf.repository.sail.SailRepository
import org.openrdf.sail.memory.MemoryStore
import utest._
import utest.framework.TestSuite
import org.w3.banana._, sesame._

object BananaPlasmidVectorRepositoryTest extends TestSuite {
  val sesameRepo = new SailRepository(new MemoryStore())
  sesameRepo.initialize()

  val repository = new BananaPlasmidVectorRepository[Sesame, RepositoryConnection](sesameRepo.getConnection)

  val growthInfo = BacteriaGrowthInfo(
          resistance = Set(Resistance("ampicillin")),
          temperature = 37.0,
          strains = Set(Strain("ecoli")),
          copyNumber = HIGH_NUMBER
        )

  val sequenceInfo = SequenceInfo(
    full = Some(DNASeq("AAAAAA")),
    partials = Set(DNASeq("CCCCC"), DNASeq("TTTTT"))
  )

  val plasmid = PlasmidVector(
    id = "some_id",
    backbone = Some("backbone_id"),
    comment = Some("comment"),
    size = 100,
    expressionType = Set(MAMMALIAN, BACTERIAL),
    selectableMarker = Set(SelectableMarker("URA")),
    growthInfo = growthInfo,
    sequenceInfo = sequenceInfo,
    cloningInfo = None,
    insert = None)
  
  val tests = TestSuite {
    "it should retrieve plasmid by id" - {
      repository.upsert(plasmid)
      assert(repository.getById(plasmid.id).get == plasmid)
    }

    "it should handle updates for existing plasmids" - {
      repository.upsert(plasmid)

      val newPlasmid = PlasmidVector(
        id = "some_id",
        backbone = Some("backbone_id"),
        comment = Some("comment"),
        size = 200,
        expressionType = Set(MAMMALIAN),
        selectableMarker = Set(SelectableMarker("Ampicilin")),
        growthInfo = growthInfo,
        sequenceInfo = sequenceInfo,
        cloningInfo = None,
        insert = None)

      repository.upsert(newPlasmid)

      val retrievedPlasmid = repository.getById(plasmid.id).get
      assert(retrievedPlasmid == newPlasmid)
    }

    "it should remove plasmid" - {
      repository.upsert(plasmid)
      repository.delete(plasmid.id)
      assert(repository.getById("absent_id").isEmpty)
    }

    "it should return None if queried with unknown id" - {
      assert(repository.getById("absent_id").isEmpty)
    }
  }
}
