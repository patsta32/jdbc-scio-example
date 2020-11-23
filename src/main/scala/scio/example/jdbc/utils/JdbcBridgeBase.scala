package scio.example.jdbc.utils

import com.spotify.scio.ScioContext
import com.spotify.scio.jdbc.JdbcConnectionOptions

abstract class JdbcBridgeBase {

  private def getJdbcUrl(connectionName: String, dbName: String): String =
    s"jdbc:postgresql://${connectionName}/${dbName}"

  private def getReadConnectionOptions(opts: ReadSqlOptions): JdbcConnectionOptions =
    JdbcConnectionOptions(
      username = opts.getReadSqlUsername,
      password = Some(opts.getReadSqlPassword),
      driverClass = classOf[org.postgresql.Driver],
      connectionUrl = getJdbcUrl(opts.getReadSqlInstanceConnectionName, opts.getReadSqlDb)
    )

  private def getWriteConnectionOptions(opts: WriteSqlOptions): JdbcConnectionOptions =
    JdbcConnectionOptions(
      username = opts.getWriteSqlUsername,
      password = Some(opts.getWriteSqlPassword),
      driverClass = classOf[org.postgresql.Driver],
      connectionUrl = getJdbcUrl(opts.getWriteSqlInstanceConnectionName, opts.getWriteSqlDb)
    )

  // Read, Write, ScioContext
  def initializeFromCmdLineArgs(cmdlineArgs: Array[String]): (JdbcConnectionOptions, JdbcConnectionOptions, ScioContext) = {
    val (readOptions, _) = ScioContext.parseArguments[ReadSqlOptions](cmdlineArgs)
    val (writeOptions, _) = ScioContext.parseArguments[WriteSqlOptions](cmdlineArgs)

    val sc = ScioContext(readOptions)

    val readConnectionOptions = getReadConnectionOptions(readOptions)
    val writeConnectionOptions = getWriteConnectionOptions(writeOptions)

    (readConnectionOptions, writeConnectionOptions, sc)
  }

}
