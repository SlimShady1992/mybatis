package dao;

import po.User;

public interface UserDao {

  /**
   * 根据用户Id查询用户信息
   */
  User queryUserById2(User param);
}
