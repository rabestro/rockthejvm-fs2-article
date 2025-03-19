import Actors.jlActors
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

object MainSample4A extends IOApp {
  val savedJlActors: Stream[IO, Int] =
    jlActors.evalMap(ActorRepository.save)

  val acquire: IO[DatabaseConnection] = IO {
    val conn = DatabaseConnection("jlaConnection")
    println(s"Acquiring connection to the database: $conn")
    conn
  }

  val release: DatabaseConnection => IO[Unit] = (conn: DatabaseConnection) =>
    IO.println(s"Releasing connection to the database: $conn")


  override def run(args: List[String]): IO[ExitCode] = {
    val managedJlActors: Stream[IO, Int] =
      Stream.bracket(acquire)(release).flatMap(_ => savedJlActors)

    managedJlActors.compile.drain.as(ExitCode.Success)
  }
}
