import Actors.{avengersActors, jlActors}
import Model.Actor
import cats.effect.IO
import fs2.{Pipe, Stream}

val liftedJlActors: Stream[IO, Actor] = jlActors.covary[IO]

val concurrentJlActors: Stream[IO, Actor] = liftedJlActors.evalMap(actor => IO {
  Thread.sleep(400)
  actor
})

val liftedAvengersActors: Stream[IO, Actor] = avengersActors.covary[IO]

val concurrentAvengersActors: Stream[IO, Actor] = liftedAvengersActors.evalMap(actor => IO {
  Thread.sleep(200)
  actor
})

def toConsole[T]: Pipe[IO, T, Unit] = in =>
  in.evalMap(str => IO.println(str))

val mergedHeroesActors: Stream[IO, Unit] =
  concurrentJlActors.merge(concurrentAvengersActors).through(toConsole)

import cats.effect.unsafe.implicits.global
mergedHeroesActors.compile.drain.unsafeRunSync()

