package sqlnode;

import java.util.ArrayList;
import java.util.List;
import sqlsource.DynamicContext;

public class MixedSqlNode implements SqlNode {

  private List<SqlNode> sqlNodes = new ArrayList<>();

  public MixedSqlNode(List<SqlNode> sqlNodes) {
    this.sqlNodes = sqlNodes;
  }

  @Override
  public void apply(DynamicContext context) {
    // 每种不同的SqlNode会进入各自的apply方法
    for (SqlNode sqlNode : sqlNodes) {
      sqlNode.apply(context);
    }
  }
}
