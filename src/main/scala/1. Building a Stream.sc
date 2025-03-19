import Actors.jlActors
import Data._
import Model.Actor
import cats.MonadThrow
import cats.effect.{ExitCode, IO, IOApp}
import fs2.{Chunk, Pure, Stream}

val tomHollandStream: Stream[Pure, Actor] = Stream.emit(tomHolland)

val spiderMen: Stream[Pure, Actor] = Stream.emits(List(
  tomHolland,
  tobeyMaguire,
  andrewGarfield
))

val jlActorList: List[Actor] = jlActors.toList
val jlActorVector: Vector[Actor] = jlActors.toVector

val infiniteJlActors: Stream[Pure, Actor] = jlActors.repeat
val repeatedJLActorsList: List[Actor] = infiniteJlActors.take(12).toList

val liftedJlActors: Stream[IO, Actor] = jlActors.covary[IO]

def jlActorStream[F[_]: MonadThrow]: Stream[F, Actor] = jlActors.covary[F]

// persist an actor through a stream:
val savingTomHolland: Stream[IO, Unit] = Stream.eval {
  IO {
    println(s"Saving actor $tomHolland")
    Thread.sleep(1000)
    println("Finished")
  }
}

// we need to compile the stream into a single instance of the effect
val compiledStream: IO[Unit] = savingTomHolland.compile.drain

// we can transform the compiled stream into an effect containing a List:
val jlActorsEffectfulList: IO[List[Actor]] = liftedJlActors.compile.toList

import cats.effect.unsafe.implicits.global
savingTomHolland.compile.drain.unsafeRunSync()

val avengersActors: Stream[Pure, Actor] = Stream.chunk(Chunk.array(Array(
  scarlettJohansson,
  robertDowneyJr,
  chrisEvans,
  markRuffalo,
  chrisHemsworth,
  jeremyRenner
)))





