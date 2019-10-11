package dao;

import po.User;

/**
 * 持久层代码
 *
 * @author think
 */
public class UserDaoImpl implements UserDao {

  // 等待注入
  private SqlSessionFactory sqlSessionFactory;

  public UserDaoImpl(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  public UserDaoImpl() {
  }

  @Override
  public User queryUserById2(User param) {
    // sqlsession被调用次数很多，而且它需要Configuration对象
    // 可以考虑使用工厂来屏蔽SqlSession的构造细节
    SqlSession sqlSession = sqlSessionFactory.openSqlSession();
    User user = sqlSession.selectOne("test.findUserById", param);
    return user;
  }

}
