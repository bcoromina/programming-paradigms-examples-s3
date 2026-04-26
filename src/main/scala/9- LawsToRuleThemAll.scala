import scala.util.Random

trait KVRepo[K,V] {
  def get(k: K): Option[V]
  def put(k: K, v: V): Unit
}
// KVRepo is not an algebra if it doesn't satisfy the following laws:
object KVRepoLaws: 
  def putThenGetLaw[K, V](repo: KVRepo[K, V], k: K, v: V): Boolean = {
    repo.put(k, v)
    repo.get(k).contains(v)
  }

  def overwriteLaw[K, V](repo: KVRepo[K, V], k: K, v1: V, v2: V): Boolean = {
    repo.put(k, v1)
    repo.put(k, v2)
    repo.get(k).contains(v2)
  }

  def emptyGetLaw[K, V](repo: KVRepo[K, V], k: K): Boolean = {
    repo.get(k).isEmpty
  }

  def laws[K, V]: List[(KVRepo[K, V], K, V) => Boolean] = List(
    (repo, k, v) => KVRepoLaws.putThenGetLaw(repo, k, v),
    (repo, k, v) => KVRepoLaws.overwriteLaw(repo, k, v, v),
    (repo, k, v) => KVRepoLaws.emptyGetLaw(repo, k)
  )

  def checkLawsForRepo[K,V](
                             newRepo: () => KVRepo[K,V],
                             randomK: () => K,
                             randomV: () => V
                           ): Boolean =
    KVRepoLaws.laws[K,V]
      .map( law => law(newRepo(), randomK(), randomV()))
      .forall(identity)
      
// Now we have our type KVRepo[K,V] and our laws so we have an algebra.  

// Example implementation of KVRepo
class InMemoryKVRepo[K,V] extends KVRepo[K,V] {
  private val store = scala.collection.mutable.Map.empty[K,V]

  override def get(k: K): Option[V] = store.get(k)

  override def put(k: K, v: V): Unit = store.put(k,v)
}

object RandomUtils:
  def randomString(length: Int): String = 
    val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    Random.alphanumeric.filter(chars.contains(_)).take(length).mkString

@main def KVRepoLawsMain() =
  def checkLawsForInMemoryRepo(): Boolean =
    KVRepoLaws.laws[String, Int]
      .map(law => law(new InMemoryKVRepo[String, Int](), "hola", 1))
      .forall(identity)

  assert(checkLawsForInMemoryRepo())

  // using generic check
  val lawCheckResult = KVRepoLaws.checkLawsForRepo[String, Int](
    () => new InMemoryKVRepo[String, Int](),
    () => RandomUtils.randomString(Random.nextInt(10)),
    () => Random.nextInt()
  )

  assert(lawCheckResult)

  // non lawful KVRepo
  assert(
    !KVRepoLaws.checkLawsForRepo[String, Int](
      () => new KVRepo[String, Int]() {
        private val store = scala.collection.mutable.Map.empty[String, Int]

        override def get(k: String): Option[Int] = store.get(k).map(_ + 1)

        override def put(k: String, v: Int): Unit = store.put(k, v)
      },
      () => RandomUtils.randomString(Random.nextInt(10)),
      () => Random.nextInt()
    )
  )