package sqlsource;

import sqlnode.MixedSqlNode;
import sqlnode.SqlNode;

/**
 * 专门封装和处理带有${}和动态sql标签的sql语句
 */
public class DynamicSqlSource implements SqlSource {

  private SqlNode rootSqlNode;

  public DynamicSqlSource(MixedSqlNode rootSqlNode) {
    this.rootSqlNode = rootSqlNode;
  }

  @Override
  public BoundSql getBoundSql(Object param) {
    // 首先先调用SqlNode的处理，将动态标签和${}处理一下
    DynamicContext context = new DynamicContext(param);
    rootSqlNode.apply(context);

    // 再调用SqlSourceParser来处理#{}
    SqlSourceParser sqlSourceParser = new SqlSourceParser();
    SqlSource sqlSource = sqlSourceParser.parse(context.getSql());
    return sqlSource.getBoundSql(param);
  }
}
