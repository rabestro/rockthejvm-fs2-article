import Actors.jlActors
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

object MainSample3A extends IOApp {
  val savedJlActors: Stream[IO, Int] =
    jlActors.evalMap(ActorRepository.save)

  override def run(args: List[String]): IO[ExitCode] =
    savedJlActors
      .compile.drain.as(ExitCode.Success)
}
