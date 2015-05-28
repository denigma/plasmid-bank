package club.diybio.bank.domain.bio

import utest._
import utest.framework.TestSuite
import org.w3.banana._, sesame._

object BananaPlasmidVectorRepositoryTest extends TestSuite {
  val rdfOps = RDFOps[Sesame]
  import rdfOps._
  
  val tests = TestSuite {
    "it should retrieve plasmid by id" - {
      val foaf = FOAFPrefix[Sesame]

      val graph = (
        URI("http://bank.diybio.club/plasmid/testid")
        -- foaf.name ->- "Test plasmid"
      ).graph

      val repository = new BananaPlasmidVectorRepository[Sesame, Sesame#Graph](graph)

      val plasmid = repository.getById("testid")

      assert(plasmid.get == PlasmidVector("testid", "Test plasmid"))
    }

    "it should return None if queried with unknown id" - {
      val repository = new BananaPlasmidVectorRepository[Sesame, Sesame#Graph](emptyGraph)
      assert(repository.getById("absentid").isEmpty)
    }
  }
}
