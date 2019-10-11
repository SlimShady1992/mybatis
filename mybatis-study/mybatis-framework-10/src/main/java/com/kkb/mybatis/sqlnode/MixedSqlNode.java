package com.kkb.mybatis.sqlnode;

import java.util.ArrayList;
import java.util.List;

import com.kkb.mybatis.sqlsource.DynamicContext;

public class MixedSqlNode implements SqlNode {
	private List<SqlNode> sqlNodes = new ArrayList<SqlNode>();

	public MixedSqlNode(List<SqlNode> sqlNodes) {
		this.sqlNodes = sqlNodes;
	}

	@Override
	public void apply(DynamicContext context) {
		for (SqlNode sqlNode : sqlNodes) {
			sqlNode.apply(context);
		}
	}

}
