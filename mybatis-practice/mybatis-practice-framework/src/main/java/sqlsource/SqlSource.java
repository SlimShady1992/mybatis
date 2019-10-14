package sqlsource;

/**
 * 接口主要是提供功能的 可以获取被JDBC程序直接执行的Sql语句
 */
public interface SqlSource {

  BoundSql getBoundSql(Object param);
}
