package com.kkb.mybatis.sqlnode;

import com.kkb.mybatis.sqlsource.DynamicContext;
import com.kkb.mybatis.utils.GenericTokenParser;
import com.kkb.mybatis.utils.OgnlUtils;
import com.kkb.mybatis.utils.SimpleTypeRegistry;
import com.kkb.mybatis.utils.TokenHandler;

public class TextSqlNode implements SqlNode {

	private String sqlText;

	public TextSqlNode(String sqlText) {
		this.sqlText = sqlText;
	}

	/**
	 * 要处理${} 比如说username like '%${username}' 入参username=“zhangsan” 处理之后username
	 * like '%zhangsan'
	 */
	@Override
	public void apply(DynamicContext context) {
		TokenHandler tokenHandler = new BindingTokenParser(context);
		GenericTokenParser tokenParser = new GenericTokenParser("${", "}", tokenHandler);
		// tokenParser.parse(sqlText)参数是未处理的，返回值是已处理的（没有${}）
		context.appendSql(tokenParser.parse(sqlText));
	}

	public boolean isDynamic() {
		if (sqlText.indexOf("${") > -1) {
			return true;
		}
		return false;
	}

	private static class BindingTokenParser implements TokenHandler {
		private DynamicContext context;

		public BindingTokenParser(DynamicContext context) {
			this.context = context;
		}

		/**
		 * expression：比如说${username}，那么expression就是username username也就是Ognl表达式
		 */
		@Override
		public String handleToken(String expression) {
			Object paramObject = context.getBindings().get("_parameter");
			if (paramObject == null) {
				// context.getBindings().put("value", null);
				return "";
			} else if (SimpleTypeRegistry.isSimpleType(paramObject.getClass())) {
				// context.getBindings().put("value", paramObject);
				return String.valueOf(paramObject);
			}

			// 使用Ognl api去获取相应的值
			Object value = OgnlUtils.getValue(expression, context.getBindings());
			String srtValue = value == null ? "" : String.valueOf(value);
			return srtValue;
		}

	}

}
