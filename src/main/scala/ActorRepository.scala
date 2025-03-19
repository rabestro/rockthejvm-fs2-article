import Model.Actor
import cats.effect.IO

import scala.util.Random

object ActorRepository {
  def save(actor: Actor): IO[Int] = IO {
    println(s"Saving actor: $actor")
    if (Random.nextInt() % 2 == 0) {
      throw new RuntimeException("Something went wrong during the communication with the persistence layer")
    }
    println(s"Saved.")
    actor.id
  }
}