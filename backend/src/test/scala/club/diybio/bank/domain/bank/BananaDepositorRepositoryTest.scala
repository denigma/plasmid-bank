package club.diybio.bank.domain.bank

import org.openrdf.repository.RepositoryConnection
import org.openrdf.repository.sail.SailRepository
import org.openrdf.sail.memory.MemoryStore
import org.w3.banana.sesame.Sesame
import utest._
import utest.framework.TestSuite

object BananaDepositorRepositoryTest extends TestSuite {

  val sesameRepo = new SailRepository(new MemoryStore())
  sesameRepo.initialize()

  val repository = new BananaDepositorRepository[Sesame, RepositoryConnection](sesameRepo.getConnection)

  val depositor = Depositor(id="1", name="Some depositor", email="email@heaven.com",
    location=Location(state="Ukraine", city="Kiev"))
  
  val tests = TestSuite {
    "it should retrieve depositor by id" - {
      repository.upsert(depositor)
      assert(repository.getById(depositor.id).get == depositor)
    }

    "it should handle updates for existing depositors" - {
      repository.upsert(depositor)

      val newDepositor = Depositor(id="1", name="Some other depositor", email="email@heck.com",
          location=Location(state="Ukraine", city="Kiev"))

      repository.upsert(newDepositor)

      val retrievedDepositor = repository.getById(newDepositor.id).get
      assert(retrievedDepositor == newDepositor)
    }

    "it should remove depositor" - {
      repository.upsert(depositor)
      repository.delete(depositor.id)
      assert(repository.getById(depositor.id).isEmpty)
    }

    "it should return None if queried with unknown id" - {
      assert(repository.getById("absent_id").isEmpty)
    }
  }
}
