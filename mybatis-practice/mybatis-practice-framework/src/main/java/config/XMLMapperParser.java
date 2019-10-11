package config;

import java.util.List;
import org.dom4j.Element;

public class XMLMapperParser {

  private Configuration configuration;

  public XMLMapperParser(Configuration configuration) {
    this.configuration = configuration;
  }

  public void parse(Element rootElement) {
    String namespace = rootElement.attributeValue("namespace");
    // 此处只对select进行处理
    List<Element> selectEles = rootElement.elements("select");
    for (Element selectEle : selectEles) {
      // select update delete insert 都对应一个statement
      // 单独对statement进行处理
      XMLStatementParser xmlStatementParser = new XMLStatementParser(configuration);
      xmlStatementParser.parseStatement(selectEle, namespace);
    }
  }
}
