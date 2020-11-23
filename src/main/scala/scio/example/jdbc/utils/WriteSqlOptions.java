package scio.example.jdbc.utils;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

public interface WriteSqlOptions extends PipelineOptions {

    @Description("Write SQL database username")
    @Validation.Required
    String getWriteSqlUsername();

    void setWriteSqlUsername(String value);

    @Description("Write SQL database password")
    String getWriteSqlPassword();

    void setWriteSqlPassword(String value);

    @Description("Write SQL database name")
    @Validation.Required
    String getWriteSqlDb();

    void setWriteSqlDb(String value);

    @Description("Write SQL instance connection name, i.e project-id:zone:db-instance-name")
    @Validation.Required
    String getWriteSqlInstanceConnectionName();

    void setWriteSqlInstanceConnectionName(String value);
    
}
