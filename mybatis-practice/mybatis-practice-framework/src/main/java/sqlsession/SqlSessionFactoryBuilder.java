package sqlsession;

import config.Configuration;
import config.XMLConfigParser;
import java.io.InputStream;
import org.dom4j.Document;
import utils.DocumentUtils;

/**
 * 使用构建者模式对SqlSessionFactory进行创建
 */
public class SqlSessionFactoryBuilder {

  public SqlSessionFactory build(InputStream inputStream) {
    // 获取Configuration对象
    Document document = DocumentUtils.readDocument(inputStream);
    XMLConfigParser configParser = new XMLConfigParser();
    Configuration configuration = configParser.parse(document.getRootElement());
    return build(configuration);
  }

  private SqlSessionFactory build(Configuration configuration) {
    return new DefaultSqlSessionFactory(configuration);
  }
}
