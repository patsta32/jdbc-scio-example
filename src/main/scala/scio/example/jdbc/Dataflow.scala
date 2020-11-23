package scio.example.jdbc

import com.spotify.scio.jdbc.{JdbcConnectionOptions, JdbcReadOptions, JdbcWriteOptions, jdbcSCollectionOps, jdbcScioContextOps}
import com.spotify.scio.values.SideOutput
import scio.example.jdbc.utils.JdbcBridgeBase


object Dataflow extends JdbcBridgeBase {

  def main(cmdlineArgs: Array[String]): Unit = {
    val optionsAndScioContext = initializeFromCmdLineArgs(cmdlineArgs)

    val sc = optionsAndScioContext._3
    
    val readPersons = getReadOptions(optionsAndScioContext._1)
    
    val writePersons = getPersonWriteOptions(optionsAndScioContext._2)
    val writeEmail = getEmailWriteOptions(optionsAndScioContext._2)

    val emailSideOutput = SideOutput[String]()
    val personSideOutput = SideOutput[Person]()
    
    val scRead = sc.jdbcSelect(readPersons)

    val (collection, sideOutputs) = scRead.withSideOutputs(emailSideOutput, personSideOutput).flatMap((person, ctx) => {
        if(!person.email.isBlank) {
          ctx.output(emailSideOutput, person.email)
        } 
        ctx.output(personSideOutput, person)
        Some(person)
    })

    sideOutputs(emailSideOutput).saveAsJdbc(writeEmail)
    sideOutputs(personSideOutput).saveAsJdbc(writePersons)

    collection.count.map(c => println(s"Processed $c Entries"))

    sc.run()
    ()
  }
  
  case class Person(id: Long, name: String, email: String)

  def getReadOptions(connOpts: JdbcConnectionOptions): JdbcReadOptions[Person] =
    JdbcReadOptions(
      connectionOptions = connOpts,
      query = """SELECT id, name, email FROM person;""".stripMargin,
      rowMapper = r => Person(r.getLong(1), r.getString(2), r.getString(3))
    )

  def getEmailWriteOptions(connOpts: JdbcConnectionOptions): JdbcWriteOptions[String] =
    JdbcWriteOptions(
      connectionOptions = connOpts,
      statement = "INSERT INTO emails (email) VALUES (?);",
      preparedStatementSetter = (kv, s) => {
        s.setString(1, kv)
      }
    )

  def getPersonWriteOptions(connOpts: JdbcConnectionOptions): JdbcWriteOptions[Person] =
    JdbcWriteOptions(
      connectionOptions = connOpts,
      statement = "INSERT INTO persons (id, name) VALUES (?,?);",
      preparedStatementSetter = (kv, s) => {
        s.setLong(1, kv.id)
        s.setString(1, kv.name)
      }
    )

}
