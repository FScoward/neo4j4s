import com.typesafe.config.ConfigFactory
import org.neo4j.driver.v1.{Session, AuthTokens, GraphDatabase}

/**
  * Created by FScoward on 2016/04/30.
  */
object Main {

  def main(arg: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val password = config.getString("neo4j.password")

    import collection.JavaConversions._
    val driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", password))
    implicit val session: Session = driver.session()
//    session.run("CREATE (a: Location {name: 'Shibuya'})")
//    session.run("CREATE (a: Location {name: 'Shinjuku'})")
//    val result = session.run( "MATCH (a:Person) WHERE a.name = 'Arthur' RETURN a.name AS name, a.title AS title" )

    clear
    create

    val confirmQ = "MATCH (n) RETURN n"
    val result = session.run(confirmQ)
    result.foreach(println)

    session.close()
    driver.close()
  }

  def create(implicit session: Session) = {
    val query =
      """
        |CREATE (a:Location { name:"Shibuya" })
        |-[r: CONNECT_TO { roles: ["NETWORK"]}]
        |->(m: Location { name:"Shinjuku" })
        |CREATE (b: Location { name: "Yokohama" })
        |-[s: CONNECT_TO { roles: ["NETWORK"] }]
        |->(m)
        |CREATE (c: Location { name: "Tokyo" })
        |CREATE (m)
        |-[t: CONNECT_TO { roles: ["NETWORK"] }]
        |->(c)
      """.stripMargin

    session.run(query)

  }

  def clear(implicit session: Session) = {
    session.run("MATCH (n) - [r] -> (j) DELETE n, r, j")
    session.run("MATCH (n) DELETE n")
  }
}
