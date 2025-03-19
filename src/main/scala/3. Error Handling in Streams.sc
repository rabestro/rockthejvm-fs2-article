import Actors.jlActors
import cats.effect.IO
import fs2.Stream


val savedJlActors: Stream[IO, Int] = jlActors.evalMap(ActorRepository.save)

import cats.effect.unsafe.implicits.global

savedJlActors.compile.drain.unsafeRunSync()