
sealed trait ConnectionState
sealed trait Open extends ConnectionState
sealed trait Closed extends ConnectionState

case class Connection[S <: ConnectionState]()

@main def phantomTypes(): Unit =
  def open(conn: Connection[Closed]): Connection[Open] =
    println("opening")
    Connection[Open]()

  def close(conn: Connection[Open]): Connection[Closed] =
    //if(conn.isClosed) throw new IllegalStateException("Connection is already closed")
    println("closing")
    Connection[Closed]()

  def execute(conn: Connection[Open], sql: String): Unit =
    println(s"Executing: $sql")
    
  open(Connection[Closed]())  