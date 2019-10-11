package com.kkb.mybatis.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;

import com.kkb.mybatis.sqlnode.IfSqlNode;
import com.kkb.mybatis.sqlnode.MixedSqlNode;
import com.kkb.mybatis.sqlnode.SqlNode;
import com.kkb.mybatis.sqlnode.StaticTextSqlNode;
import com.kkb.mybatis.sqlnode.TextSqlNode;
import com.kkb.mybatis.sqlnode.handler.NodeHandler;
import com.kkb.mybatis.sqlsource.DynamicSqlSource;
import com.kkb.mybatis.sqlsource.RawSqlSource;
import com.kkb.mybatis.sqlsource.SqlSource;

/**
 * 创建SqlSource其实就是对select等CRUD标签中的sql脚本进行处理
 * 
 * @author 灭霸詹
 *
 */
public class XMLScriptParser {
	private Map<String, NodeHandler> nodeHandlerMap = new HashMap<String, NodeHandler>();

	private boolean isDynamic = false;

	public XMLScriptParser() {
		initNodeHandlerMap();
	}

	private void initNodeHandlerMap() {
		nodeHandlerMap.put("if", new IfNodeHandler());
		// nodeHandlerMap.put("where", new WhereNodeHandler());
		// nodeHandlerMap.put("foreach", new ForeachNodeHandler());
	}

	public SqlSource parseScriptNode(Element selectElement) {
		// 首先先将sql脚本按照不同的类型，封装到不同的SqlNode
		MixedSqlNode rootSqlNode = parseDynamicTags(selectElement);
		// 再将SqlNode集合封装到SqlSource中
		SqlSource sqlSource = null;
		if (isDynamic) {
			sqlSource = new DynamicSqlSource(rootSqlNode);
		} else {
			sqlSource = new RawSqlSource(rootSqlNode);
		}
		// 由于带有#{}和${}、动态标签的sql处理方式不同，所以需要封装到不同的SqlSource中
		return sqlSource;
	}

	private MixedSqlNode parseDynamicTags(Element selectElement) {
		List<SqlNode> contents = new ArrayList<SqlNode>();
		int nodeCount = selectElement.nodeCount();
		for (int i = 0; i < nodeCount; i++) {
			Node node = selectElement.node(i);
			// 需要去区分select标签的子节点类型
			// 如果是文本类型则封装到TextSqlNode或者StaticTextSqlNode
			if (node instanceof Text) {
				String sqlText = node.getText().trim();
				if (sqlText == null || sqlText.equals("")) {
					continue;
				}
				TextSqlNode sqlNode = new TextSqlNode(sqlText);
				// 判断文本中是否带有${}
				if (sqlNode.isDynamic()) {
					contents.add(sqlNode);
					isDynamic = true;
				} else {
					contents.add(new StaticTextSqlNode(sqlText));
				}
			} else if (node instanceof Element) {// 则递归解析
				// 比如说if\where\foreach等动态sql子标签就需要在这处理
				// 根据标签名称，封装到不同的节点信息
				Element nodeToHandle = (Element) node;
				String nodeName = nodeToHandle.getName().toLowerCase();
				// 每一种动态标签都应该有相应的处理逻辑
				// if("if".equals(nodeName)){
				//	NodeHandler nodeHandler =new IfNodeHandler();
				//  nodeHandler.handleNode(nodeToHandle, contents);
				//}	
				NodeHandler nodeHandler = nodeHandlerMap.get(nodeName);
				nodeHandler.handleNode(nodeToHandle, contents);

				isDynamic = true;
			}
		}
		return new MixedSqlNode(contents);
	}

	public class IfNodeHandler implements NodeHandler {

		@Override
		public void handleNode(Element nodeToHandle, List<SqlNode> contents) {
			// 解析test中表达式
			String test = nodeToHandle.attributeValue("test");
			// 解析if标签（其实也就是解析子标签）
			MixedSqlNode rootSqlNode = parseDynamicTags(nodeToHandle);

			//将解析到的内容封装成一个SqlNode（只是这个SqlNode还包含子SqlNode）
			IfSqlNode ifSqlNode = new IfSqlNode(test, rootSqlNode);
			
			contents.add(ifSqlNode);
		}

	}
}
