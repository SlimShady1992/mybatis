package config;

import org.dom4j.Element;
import sqlsource.SqlSource;
import utils.ReflectUtils;

public class XMLStatementParser {

  private Configuration configuration;

  public XMLStatementParser(Configuration configuration) {
    this.configuration = configuration;
  }

  public void parseStatement(Element selectEle, String namespace) {
    String id = selectEle.attributeValue("id");
    String parameterType = selectEle.attributeValue("parameterType");
    Class<?> parameterTypeClass = ReflectUtils.resolveClass(parameterType);
    String resultType = selectEle.attributeValue("resultType");
    Class<?> resultTypeClass = ReflectUtils.resolveClass(resultType);
    String statementType = selectEle.attributeValue("statementType");
    statementType = statementType == null || statementType.equals("") ? "prepared" : statementType;

    SqlSource sqlSource = createSqlSource(selectEle);

    String statementId = namespace + "." + id;

    MappedStatement mappedStatement = new MappedStatement(statementId, parameterTypeClass,
        resultTypeClass,
        statementType, sqlSource);

    // TODO mappedStatements 是在这一步设置的
    configuration.setMappedStatements(statementId, mappedStatement);
  }

  private SqlSource createSqlSource(Element selectEle) {
    // 剩下sql脚本未解析，交给XMLScriptParser解析
    XMLScriptParser scriptParser = new XMLScriptParser();
    SqlSource sqlSource = scriptParser.parseScriptNode(selectEle);
    return sqlSource;
  }
}
