package config;


import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.Element;
import utils.DocumentUtils;
import utils.Resources;

public class XMLConfigParser {

  private Configuration configuration;

  public XMLConfigParser() {
    this.configuration = new Configuration();
  }

  /**
   * 解析SqlMapConfig.xml获取Configuration对象
   */
  public Configuration parse(Element rootElement) {
    parseEnvironments(rootElement.element("environments"));
    parseMappers(rootElement.element("mappers"));
    return configuration;
  }

  private void parseMappers(Element mappers) {
    List<Element> elements = mappers.elements("mapper");
    for (Element mapperElement : elements) {
      parseMapper(mapperElement);
    }
  }

  private void parseMapper(Element mapperElement) {
    // 获取映射文件的路径
    String resource = mapperElement.attributeValue("resource");
    // 获取指定路径的IO流
    InputStream inputStream = Resources.getResourceAsStream(resource);
    // 获取映射文件对应的Document对象
    Document document = DocumentUtils.readDocument(inputStream);
    // 按照mapper.xml标签语义去解析Document
    // 之所以将configuration对象传入XMLMapperParser,是因为只能有一个configuration对象
    XMLMapperParser xmlMapperParser = new XMLMapperParser(configuration);
    xmlMapperParser.parse(document.getRootElement());

  }

  private void parseEnvironments(Element environments) {
    String defaultEnvId = environments.attributeValue("default");
    if (defaultEnvId == null || "".equals(defaultEnvId)) {
      return;
    }
    List<Element> environment = environments.elements("environment");
    for (Element envElement : environment) {
      String envId = envElement.attributeValue("id");
      // 判断defaultEnvId和envId是否一致，一致再继续解析
      if (defaultEnvId.equals(envId)) {
        parseEnvironment(envElement);
      }
    }
  }

  private void parseEnvironment(Element environment) {
    Element dataSourceEle = environment.element("dataSource");
    String type = dataSourceEle.attributeValue("type");
    // 给type一个默认值,默认为DBCP
    type = type == null || "".equals(type) ? "DBCP" : type;
    // 判断的原因是还有其他连接方式，这里只写了一种而已，意思意思就行
    if ("DBCP".equals(type)) {
      Properties properties = parseProperty(dataSourceEle);

      BasicDataSource dataSource = new BasicDataSource();
      dataSource.setDriverClassName(properties.getProperty("driver"));
      dataSource.setUrl(properties.getProperty("url"));
      dataSource.setUsername(properties.getProperty("username"));
      dataSource.setPassword(properties.getProperty("password"));

      // TODO Datasource是在这一步设置的
      configuration.setDataSource(dataSource);
    }

  }

  private Properties parseProperty(Element dataSourceEle) {
    Properties properties = new Properties();
    List<Element> propertys = dataSourceEle.elements("property");
    for (Element property : propertys) {
      String name = property.attributeValue("name");
      String value = property.attributeValue("value");

      properties.put(name, value);
    }
    return properties;
  }

}
