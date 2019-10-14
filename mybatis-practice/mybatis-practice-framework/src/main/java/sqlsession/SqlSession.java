package sqlsession;

import java.util.List;

/**
 * 表示一个sql会话，就是一次CRUD操作
 */
public interface SqlSession {

  <T> T selectOne(String statementId, Object param);

  <T> List<T> selectList(String statementId, Object param);
}
