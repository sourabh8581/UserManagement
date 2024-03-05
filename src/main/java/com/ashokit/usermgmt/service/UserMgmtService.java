package com.ashokit.usermgmt.service;

import com.ashokit.usermgmt.bindings.ActivateAccount;
import com.ashokit.usermgmt.bindings.Login;
import com.ashokit.usermgmt.bindings.User;

import java.util.List;

public interface UserMgmtService {
    public boolean saveUser(User user);
    public boolean activateUserAccount(ActivateAccount activateAccount);
    public List<User> getAllUsers();
    public User getUserById(Integer userId);
    public User getUserByEmail(String email);
    public boolean deleteUserById(Integer userId);
    public boolean changeAccountStatus(Integer userId , String accStatus);
    public String login(Login login);
    public String forgotPwd(String email);
}
