import Actors.jlActors
import cats.effect.IO
import fs2.{Chunk, Pipe, Pure, Stream}

// Error Handling in Streams

val savedJlActors: Stream[IO, Int] = jlActors.evalMap(ActorRepository.save)

