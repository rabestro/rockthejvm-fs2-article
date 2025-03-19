import Actors.jlActors
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

object MainSample3 extends IOApp {
  val savedJlActors: Stream[IO, Int] =
    jlActors.evalMap(ActorRepository.save)

  private val errorHandledSavedJlActors: Stream[IO, AnyVal] =
    savedJlActors.handleErrorWith(error => Stream.eval(IO.println(s"Error: $error")))

  override def run(args: List[String]): IO[ExitCode] =
    errorHandledSavedJlActors
      .compile.drain.as(ExitCode.Success)
}
