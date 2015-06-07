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
  
  val tests = TestSuite {
    "it should retrieve plasmid by id" - {
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

      repository.upsert(plasmid)
      assert(repository.getById(plasmid.id).get == plasmid)
    }

    "it should return None if queried with unknown id" - {
      assert(repository.getById("absent_id").isEmpty)
    }
  }
}
