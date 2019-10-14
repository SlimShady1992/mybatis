package executor;

import config.Configuration;
import config.MappedStatement;
import java.util.List;

public interface Executor {

  /**
   * @param mappedStatement 获取sql语句和出入参等信息
   * @param configuration 获取数据源对象
   * @param param 入参对象
   */
  <T> List<T> query(MappedStatement mappedStatement, Configuration configuration, Object param);
}
