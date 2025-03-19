import Actors.jlActors
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

object MainSample4 extends IOApp {
  val savedJlActors: Stream[IO, Int] =
    jlActors.evalMap(ActorRepository.save)

  val attemptedSavedJlActors: Stream[IO, Either[Throwable, Int]] = savedJlActors.attempt

  override def run(args: List[String]): IO[ExitCode] =
    attemptedSavedJlActors.evalMap {
      case Left(error) => IO.println(s"Error: $error")
      case Right(id) => IO.println(s"Saved actor with id: $id")
    }.compile.drain.as(ExitCode.Success)
}
