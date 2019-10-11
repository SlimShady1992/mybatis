package com.kkb.mybatis.sqlnode;

import com.kkb.mybatis.sqlsource.DynamicContext;

public class StaticTextSqlNode implements SqlNode {

	private String sqlText;

	public StaticTextSqlNode(String sqlText) {
		super();
		this.sqlText = sqlText;
	}

	@Override
	public void apply(DynamicContext context) {
		// sql文本追加
		context.appendSql(sqlText);
	}

}
