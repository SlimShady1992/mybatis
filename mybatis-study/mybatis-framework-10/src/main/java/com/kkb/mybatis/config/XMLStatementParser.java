package com.kkb.mybatis.config;

import org.dom4j.Element;

import com.kkb.mybatis.sqlsource.SqlSource;

public class XMLStatementParser {
	private Configuration configuration;

	public XMLStatementParser(Configuration configuration) {
		this.configuration = configuration;
	}

	public void parseStatement(Element selectElement, String namespace) {
		// 一个MappedStatement对象，就对应一个select标签
		String id = selectElement.attributeValue("id");
		String parameterType = selectElement.attributeValue("parameterType");
		Class<?> parameterTypeClass = resolveClass(parameterType);

		String resultType = selectElement.attributeValue("resultType");
		Class<?> resultTypeClass = resolveClass(resultType);

		String statementType = selectElement.attributeValue("statementType");
		statementType = statementType == null || statementType.equals("") ? "prepared" : statementType;
		// SqlSource就是封装了SQL语句
		// 此时封装的SQL语句是没有进行处理的，什么时候处理呢？
		// 处理时机，就是在SqlSession执行的时候
		// SELECT * FROM user WHERE id = #{id}
		// String sqlText = selectElement.getTextTrim();
		// SqlSource sqlSource = new SqlSource(sqlText);
		SqlSource sqlSource = createSqlSource(selectElement);

		String statementId = namespace + "." + id;

		// 此处使用构造方法或者set方法去赋值的话，感觉都不爽
		// 构造参数可能有，可能没有
		// 使用构建者模式改造
		MappedStatement mappedStatement = new MappedStatement(statementId, parameterTypeClass, resultTypeClass,
				statementType, sqlSource);
		configuration.setMappedStatement(statementId, mappedStatement);
	}

	/**
	 * 创建SqlSource其实就是对select等CRUD标签中的sql脚本进行处理
	 * @param selectElement
	 * @return
	 */
	private SqlSource createSqlSource(Element selectElement) {
		XMLScriptParser scriptParser = new XMLScriptParser();
		SqlSource sqlSource = scriptParser.parseScriptNode(selectElement);
		return sqlSource;
	}

	private Class<?> resolveClass(String parameterType) {
		try {
			return Class.forName(parameterType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
