package com.kkb.mybatis.sqlnode;

import com.kkb.mybatis.sqlsource.DynamicContext;

/**
 * 提供对sql脚本的解析
 * 
 * @author 灭霸詹
 *
 */
public interface SqlNode {

	void apply(DynamicContext context);
}
