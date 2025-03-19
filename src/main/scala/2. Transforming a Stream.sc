import Data._
import Model.Actor
import cats.effect.IO
import fs2.{Chunk, Pipe, Pure, Stream}

/**
 * Once we’ve built a Stream, we can transform its values more or less as we make in regular Scala collections.
 *
 * @see https://rockthejvm.com/articles/fs2-more-than-functional-streaming-in-scala#transforming-a-stream
 */

val jlActors: Stream[Pure, Actor] = Stream(
  henryCavil,
  galGodot,
  ezraMiller,
  benFisher,
  rayHardy,
  jasonMomoa
)

// create a stream directly through the Stream.chunk method
val avengersActors: Stream[Pure, Actor] = Stream.chunk(Chunk.array(Array(
  scarlettJohansson,
  robertDowneyJr,
  chrisEvans,
  markRuffalo,
  chrisHemsworth,
  jeremyRenner
)))

// create a stream containing all the actors whose hero belongs from the Justice League or the Avengers
val dcAndMarvelSuperheroes: Stream[Pure, Actor] = jlActors ++ avengersActors

// printing to the console the elements of a stream
val printedJlActors: Stream[IO, Unit] = jlActors.flatMap { actor =>
  Stream.eval(IO.println(actor))
}
// fs2 provides a shortcut for it through the evalMap method
val evalMappedJlActors: Stream[IO, Unit] = jlActors.evalMap(IO.println)

// we need to perform some effects on the stream,
// but we don’t want to change the type of the stream
val evalTappedJlActors: Stream[IO, Actor] = jlActors.evalTap(IO.println)

// group the Avengers by the actor’s name
val avengersActorsByFirstName: Stream[Pure, Map[String, List[Actor]]] = avengersActors.fold(Map.empty[String, List[Actor]]) { (map, actor) =>
  map + (actor.firstName -> (actor :: map.getOrElse(actor.firstName, Nil)))
}

// streaming libraries define streams and transformation in terms of sources, pipes, and sinks.

// transform the jlActors streams into a stream containing only the first names of the actors
// and finally print them to the console

val fromActorToStringPipe: Pipe[IO, Actor, String] = in =>
  in.map(actor => s"${actor.firstName} ${actor.lastName}")

def toConsole[T]: Pipe[IO, T, Unit] = in =>
  in.evalMap(str => IO.println(str))

val stringNamesOfJlActors: Stream[IO, Unit] =
  jlActors.through(fromActorToStringPipe).through(toConsole)

// The jlActors stream represents the source,
// whereas the fromActorToStringPipe represents a pipe,
// and the toConsole represents the sink.

import cats.effect.unsafe.implicits.global
stringNamesOfJlActors.compile.drain.unsafeRunSync()

