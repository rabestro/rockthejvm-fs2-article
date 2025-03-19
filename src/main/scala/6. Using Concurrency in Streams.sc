import Actors.{avengersActors, jlActors}
import Model.Actor
import cats.effect.IO
import cats.effect.std.Queue
import fs2.{Pipe, Stream}

import scala.concurrent.duration.DurationInt

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

val queue: IO[Queue[IO, Actor]] = Queue.bounded[IO, Actor](10)

val concurrentlyStreams: Stream[IO, Unit] = Stream.eval(queue).flatMap { q =>
  val producer: Stream[IO, Unit] =
    liftedJlActors
      .evalTap(actor => IO.println(s"[${Thread.currentThread().getName}] produced $actor"))
      .evalMap(q.offer)
      .metered(1.second)
  val consumer: Stream[IO, Unit] =
    Stream.fromQueueUnterminated(q)
      .evalMap(actor => IO.println(s"[${Thread.currentThread().getName}] consumed $actor"))
  producer.concurrently(consumer)
}

concurrentlyStreams.compile.drain.unsafeRunSync()