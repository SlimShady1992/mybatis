package config;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

public class Configuration {

  private DataSource dataSource;

  private Map<String, MappedStatement> mappedStatements = new HashMap<String, MappedStatement>();

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Map<String, MappedStatement> getMappedStatements() {
    return mappedStatements;
  }

  public void setMappedStatements(String statementId, MappedStatement mappedStatement) {
    mappedStatements.put(statementId, mappedStatement);
  }

  public MappedStatement getMappedStatementById(String statementId) {
    return mappedStatements.get(statementId);
  }
}
