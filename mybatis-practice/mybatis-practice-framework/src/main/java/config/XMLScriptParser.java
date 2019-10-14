package config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import sqlnode.IfSqlNode;
import sqlnode.MixedSqlNode;
import sqlnode.SqlNode;
import sqlnode.StaticTextSqlNode;
import sqlnode.TextSqlNode;
import sqlnode.handler.NodeHandler;
import sqlsource.DynamicSqlSource;
import sqlsource.RawSqlSource;
import sqlsource.SqlSource;

public class XMLScriptParser {

  private Map<String, NodeHandler> nodeHandlerMap = new HashMap<String, NodeHandler>();

  private boolean isDynamic = false;

  public XMLScriptParser() {
    initNodeHandlerMap();
  }

  private void initNodeHandlerMap() {
    nodeHandlerMap.put("if", new IfNodeHandler());
  }

  public SqlSource parseScriptNode(Element selectEle) {
    // 首先将sql脚本按照不同的类型，封装到不同的SqlNode
    MixedSqlNode rootSqlNode = parseDynamicTags(selectEle);
    // 再将SqlNode集合封装到SqlSource中
    SqlSource sqlSource = null;
    if (isDynamic) {
      sqlSource = new DynamicSqlSource(rootSqlNode);
    } else {
      sqlSource = new RawSqlSource(rootSqlNode);
    }
    // 由于带#{}和${}、动态标签的sql处理方式不同，所以需要封装到不同的SqlSource中
    return sqlSource;
  }

  private MixedSqlNode parseDynamicTags(Element selectEle) {
    List<SqlNode> contents = new ArrayList<>();
    int nodeCount = selectEle.nodeCount();
    for (int i = 0; i < nodeCount; i++) {
      Node node = selectEle.node(i);
      // 需要区分select标签的子节点类型
      // 如果是文本类型则封装到TextSqlNode或者StaticTextSqlNode
      if (node instanceof Text) {
        String sqlText = node.getText().trim();
        if (sqlText == null || "".equals(sqlText)) {
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
      } else if (node instanceof Element) {
        // 则递归解析
        // 比如说if\where\foreach等动态子标签就需要在这处理
        // 根据标签名称，封装到不同的节点信息
        Element nodeToHandle = (Element) node;
        String nodeName = nodeToHandle.getName().toLowerCase();
        // 每一种动态标签都应该有相应的处理逻辑，所以有不同的handler，这里只处理if节点
        NodeHandler nodeHandler = nodeHandlerMap.get(nodeName);
        nodeHandler.handleNode(nodeToHandle, contents);

        isDynamic = true;
      }
    }
    // 这里返回的是MixedSqlNode，
    // 所以每次都会先进MixedSqlNode的apply()方法，
    // 然后再由该方法分配不同的SqlNode进入各自的apply()方法
    return new MixedSqlNode(contents);
  }

  private class IfNodeHandler implements NodeHandler {

    @Override
    public void handleNode(Element nodeToHandle, List<SqlNode> contents) {
      // 解析test中表达式
      String test = nodeToHandle.attributeValue("test");
      // 递归解析if标签(其实也就是解析子标签)
      MixedSqlNode rootSqlNode = parseDynamicTags(nodeToHandle);
      // 将解析到的内容封装成一个SqlNode（只是这个SqlNode还包含子SqlNode）
      IfSqlNode ifSqlNode = new IfSqlNode(test, rootSqlNode);

      contents.add(ifSqlNode);
    }
  }
}
