package com.kkb.mybatis.sqlsource;

import com.kkb.mybatis.sqlnode.SqlNode;

/**
 * 该SqlSource主要是封装非动态的SqlNode信息（也就是不带有${}或者动态sql标签的sqlNode）
 * 
 * @author 灭霸詹
 *
 */
public class RawSqlSource implements SqlSource {

	private SqlSource sqlSource;

	public RawSqlSource(SqlNode rootSqlNode) {
		DynamicContext context = new DynamicContext(null);
		rootSqlNode.apply(context);
		// 在这里要先对sql节点进行解析
		SqlSourceParser sqlSourceParser = new SqlSourceParser();
		sqlSource = sqlSourceParser.parse(context.getSql());
	}

	@Override
	public BoundSql getBoundSql(Object param) {
		// 从staticSqlSource中获取相应信息
		return sqlSource.getBoundSql(param);
	}

}
