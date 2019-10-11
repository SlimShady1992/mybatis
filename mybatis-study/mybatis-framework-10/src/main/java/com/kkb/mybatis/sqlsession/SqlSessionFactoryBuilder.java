package com.kkb.mybatis.sqlsession;

import java.io.InputStream;
import java.io.Reader;

import org.dom4j.Document;

import com.kkb.mybatis.config.Configuration;
import com.kkb.mybatis.config.XMLConfigParser;
import com.kkb.mybatis.utils.DocumentUtils;

/**
 * 使用构建者模式对SqlSessionFactory进行创建
 * 
 * @author 灭霸詹
 *
 */
public class SqlSessionFactoryBuilder {

	public SqlSessionFactory build(InputStream inputStream) {
		// 获取Configuration对象
		Document document = DocumentUtils.readDocument(inputStream);
		XMLConfigParser configParser = new XMLConfigParser();
		Configuration configuration = configParser.parse(document.getRootElement());
		return build(configuration);
	}

	public SqlSessionFactory build(Reader reader) {
		return null;
	}

	private SqlSessionFactory build(Configuration configuration) {
		return new DefaultSqlSessionFactory(configuration);
	}
}
