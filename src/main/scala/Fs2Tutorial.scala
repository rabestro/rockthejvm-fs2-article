import Data.tomHolland
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

object Fs2Tutorial extends IOApp {
  // persist an actor through a stream:
  val savingTomHolland: Stream[IO, Unit] = Stream.eval {
    IO {
      println(s"Saving actor $tomHolland")
      Thread.sleep(1000)
      println("Finished")
    }
  }

  override def run(args: List[String]): IO[ExitCode] = {
    savingTomHolland.compile.drain.as(ExitCode.Success)
  }
}

