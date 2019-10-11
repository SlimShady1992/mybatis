package com.kkb.mybatis.sqlnode;

import com.kkb.mybatis.sqlsource.DynamicContext;
import com.kkb.mybatis.utils.OgnlUtils;

public class IfSqlNode implements SqlNode {

	/**
	 * 布尔表达式
	 */
	private String test;

	// 子SqlNode集合
	private SqlNode rootSqlNode;

	public IfSqlNode(String test, SqlNode rootSqlNode) {
		this.test = test;
		this.rootSqlNode = rootSqlNode;
	}

	@Override
	public void apply(DynamicContext context) {
		// 使用Ognl的api来对test标签属性中的布尔表达式进行处理，获取布尔值
		boolean evaluateBoolean = OgnlUtils.evaluateBoolean(test, context.getBindings().get("_parameter"));
		// 如果test标签属性中的表达式判断为true，才进行子节点的处理
		if (evaluateBoolean) {
			rootSqlNode.apply(context);
		}
	}

}
