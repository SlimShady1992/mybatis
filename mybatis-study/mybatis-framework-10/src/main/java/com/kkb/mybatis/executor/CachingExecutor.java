package com.kkb.mybatis.executor;

import java.util.List;

import com.kkb.mybatis.config.Configuration;
import com.kkb.mybatis.config.MappedStatement;

/**
 * 处理二级缓存
 * 
 * @author 灭霸詹
 *
 */
public class CachingExecutor implements Executor {

	// 基本执行器
	private Executor delegate;
	
	public CachingExecutor(Executor delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public <T> List<T> query(MappedStatement mappedStatement, Configuration configuration, Object param) {
		// 从二级缓存中根据sql语句获取处理结果（二级缓存怎么存？？？？？）
		// 如果有，则直接返回，如果没有则继续委托给基本执行器去吃力
		return delegate.query(mappedStatement, configuration, param);
	}

}
