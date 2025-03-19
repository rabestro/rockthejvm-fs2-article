import MainSample3A.savedJlActors
import cats.effect.IO
import fs2.Stream

val acquire = IO {
  val conn = DatabaseConnection("jlaConnection")
  println(s"Acquiring connection to the database: $conn")
  conn
}

val release = (conn: DatabaseConnection) =>
  IO.println(s"Releasing connection to the database: $conn")

val managedJlActors: Stream[IO, Int] =
  Stream.bracket(acquire)(release).flatMap(conn => savedJlActors)



