package scio.example.jdbc.utils;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

public interface ReadSqlOptions extends PipelineOptions {

    @Description("Read SQL database username")
    @Validation.Required
    String getReadSqlUsername();

    void setReadSqlUsername(String value);

    @Description("Read SQL database password")
    String getReadSqlPassword();

    void setReadSqlPassword(String value);

    @Description("Read SQL database name")
    @Validation.Required
    String getReadSqlDb();

    void setReadSqlDb(String value);

    @Description("Read SQL instance connection name, i.e project-id:zone:db-instance-name")
    @Validation.Required
    String getReadSqlInstanceConnectionName();

    void setReadSqlInstanceConnectionName(String value);
    
}
