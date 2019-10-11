package com.kkb.mybatis.test;

import java.io.InputStream;

import org.dom4j.Document;
import org.junit.Test;

import com.kkb.mybatis.config.Configuration;
import com.kkb.mybatis.config.XMLConfigParser;
import com.kkb.mybatis.dao.UserDao;
import com.kkb.mybatis.dao.UserDaoImpl;
import com.kkb.mybatis.po.User;
import com.kkb.mybatis.sqlsession.SqlSessionFactory;
import com.kkb.mybatis.sqlsession.SqlSessionFactoryBuilder;
import com.kkb.mybatis.utils.DocumentUtils;
import com.kkb.mybatis.utils.Resources;

public class UserDaoTest {

	@Test
	public void testInitConfiguration() throws Exception {
		// 指定全局配置文件路径
		String resource = "SqlMapConfig.xml";
		// 获取指定路径的IO流
		InputStream inputStream = Resources.getResourceAsStream(resource);
		// 获取Document对象
		Document document = DocumentUtils.readDocument(inputStream);
		// 解析Document获取Configuration对象
		XMLConfigParser configParser = new XMLConfigParser();

		// <configuration>
		Configuration configuration = configParser.parse(document.getRootElement());
		System.out.println(configuration);
	}

	@Test
	public void testQueryUserById() {
		UserDao userDao = new UserDaoImpl();
		User param = new User();
		param.setId(1);
		User user = userDao.queryUserById(param);
		System.out.println(user);

	}

	/**
	 * 使用手写mybatis框架去实现的
	 */
	@Test
	public void testQueryUserById2() {
		String resource = "SqlMapConfig.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		// SqlSessionFactory的创建可能有几种创建方式，但是我还是不想要知道SqlSessionFactory的构造细节
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		UserDao userDao = new UserDaoImpl(sqlSessionFactory);
		User param = new User();
		param.setId(1);
		User user = userDao.queryUserById2(param);
		System.out.println(user);
	}

}
