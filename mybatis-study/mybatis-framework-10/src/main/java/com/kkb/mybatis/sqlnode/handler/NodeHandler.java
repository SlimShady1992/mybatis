package com.kkb.mybatis.sqlnode.handler;

import java.util.List;

import org.dom4j.Element;

import com.kkb.mybatis.sqlnode.SqlNode;

/**
 * 针对不同子标签进行处理，处理之后，封装到对应的SqlNode对象中
 * 
 * 比如if标签被处理之后，会封装到IfSqlNode对象中
 * 
 * @author 灭霸詹
 *
 */
public interface NodeHandler {

	/**
	 * 处理非文本节点
	 * @param nodeToHandle 待处理子节点
	 * @param contents 处理之后的节点集合
	 */
	void handleNode(Element nodeToHandle, List<SqlNode> contents);
}
