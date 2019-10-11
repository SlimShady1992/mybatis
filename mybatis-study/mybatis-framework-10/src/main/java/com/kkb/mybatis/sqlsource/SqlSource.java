package com.kkb.mybatis.sqlsource;

/**
 * 接口主要是提供功能的
 * 可以获取被JDBC程序直接执行的Sql语句
 * @author 灭霸詹
 *
 */
public interface SqlSource {

	BoundSql getBoundSql(Object param);
}
