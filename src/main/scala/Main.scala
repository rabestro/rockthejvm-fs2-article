import Data._
import Model.Actor
import cats.effect.std.Queue
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.all._
import fs2.{Chunk, INothing, Pipe, Pull, Pure, Stream}

object Main {
  val jlActors: Stream[Pure, Actor] = Stream(
    henryCavil,
    galGodot,
    ezraMiller,
    benFisher,
    rayHardy,
    jasonMomoa
  )

  val tomHollandStream: Stream[Pure, Actor] = Stream.emit(tomHolland)

  val spiderMen: Stream[Pure, Actor] = Stream.emits(List(
    tomHolland,
    tobeyMaguire,
    andrewGarfield
  ))

  def main(args: Array[String]): Unit = {
    println("Hello world!")

    println(jlActors.toList)
  }
}