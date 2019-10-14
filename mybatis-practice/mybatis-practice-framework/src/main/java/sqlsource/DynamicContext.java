package sqlsource;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储SqlNode解析过程中产生的sql片段，并完成字符串拼接，存储SqlNode解析过程需要的入参信息
 */
public class DynamicContext {

  private StringBuilder sb = new StringBuilder();

  private Map<String, Object> bindings = new HashMap<>();

  public DynamicContext(Object param) {
    bindings.put("_parameter", param);
  }

  public void appendSql(String sql) {
    sb.append(sql);
    sb.append(" ");
  }

  public String getSql() {
    return sb.toString();
  }

  public Map<String, Object> getBindings() {
    return bindings;
  }
}
