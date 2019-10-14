package sqlsource;

import java.util.ArrayList;
import java.util.List;

/**
 * 作用：封装已经完全解析成功的sql语句和解析出来的参数信息集合
 */
public class BoundSql {

  private String sql;

  private List<ParameterMapping> parameterMappings = new ArrayList<>();

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public List<ParameterMapping> getParameterMappings() {
    return parameterMappings;
  }

  public void addParameterMappings(ParameterMapping parameterMapping) {
    this.parameterMappings.add(parameterMapping);
  }

  public BoundSql(String sql, List<ParameterMapping> parameterMappings) {
    this.sql = sql;
    this.parameterMappings = parameterMappings;
  }
}
