package sqlnode;

import sqlsource.DynamicContext;

/**
 * 提供对sql脚本的解析
 */
public interface SqlNode {

  void apply(DynamicContext context);

}
