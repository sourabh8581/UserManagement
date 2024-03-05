package com.ashokit.usermgmt.service;

import com.ashokit.usermgmt.bindings.ActivateAccount;
import com.ashokit.usermgmt.bindings.Login;
import com.ashokit.usermgmt.bindings.User;
import com.ashokit.usermgmt.entities.UserMaster;
import com.ashokit.usermgmt.repo.UserMasterRepo;
import com.ashokit.usermgmt.utils.EmailUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserMgmtImpl implements UserMgmtService{
    @Autowired
    private UserMasterRepo userMasterRepo;
    @Autowired
    private EmailUtils emailUtils;
    @Override
    public boolean saveUser(User user) {
        UserMaster entity = new UserMaster();
        BeanUtils.copyProperties(user , entity);
        entity.setAccStatus("In-Active");
        entity.setPassword(generateRandomPwd());

        UserMaster save = userMasterRepo.save(entity);
        String subject = "Registration Successfull";
        String filename = "mailbody.txt";
        //        To Do : Send email to user
        String body = readEmailBody(entity.getFullName(), entity.getPassword(),filename);

        emailUtils.sendMail(user.getEmail(), subject , body);
        return save.getUserId()!=null;
    }

    @Override
    public boolean activateUserAccount(ActivateAccount activateAccount) {
        UserMaster entity = new UserMaster();
        entity.setEmail(activateAccount.getEmail());
        entity.setPassword(activateAccount.getTempPwd());

        // Select * from User_master where email=? and password=?
        Example<UserMaster> userMasterExample = Example.of(entity);

        List<UserMaster> all = userMasterRepo.findAll(userMasterExample);
        if(all.isEmpty()){
            return false;
        }else {
            UserMaster userMaster = all.get(0);
            userMaster.setPassword(activateAccount.getNewPwd());
            userMaster.setAccStatus("Active");
            userMasterRepo.save(userMaster);
            return true;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<UserMaster> all = userMasterRepo.findAll();

        List<User> users = new ArrayList<>();
        for(UserMaster entity:all){
            User user = new User();
            BeanUtils.copyProperties(entity , user);
            users.add(user);
        }
        return users;
    }

    @Override
    public User getUserById(Integer userId) {
        Optional<UserMaster> byId = userMasterRepo.findById(userId);

        if(byId.isPresent()){
            User user = new User();
            UserMaster userMaster = byId.get();
            BeanUtils.copyProperties(userMaster , user);
            return user;
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public boolean deleteUserById(Integer userId) {
        try{
            userMasterRepo.findById(userId);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean changeAccountStatus(Integer userId, String accStatus) {
        Optional<UserMaster> byId = userMasterRepo.findById(userId);
        if(byId.isPresent()){
            UserMaster userMaster = byId.get();
            userMaster.setAccStatus(accStatus);
            return true;
        }
        return false;
    }

    @Override
    public String login(Login login) {
//        UserMaster entity = new UserMaster();
//        entity.setEmail(login.getEmail());
//        entity.setPassword(login.getPassword());
//        Select * from user_master where email=? and password=?
//        Example<UserMaster> userMasterExample = Example.of(entity);
        UserMaster byEmailAndPassword = userMasterRepo.findByEmailAndPassword(login.getEmail(), login.getPassword());

//        List<UserMaster> all = userMasterRepo.findAll(userMasterExample);
        if(byEmailAndPassword==null){
            return "Invalid Credentials";

        }else {
//            UserMaster userMaster = all.get(0);
            if(byEmailAndPassword.getAccStatus().equals("Active")){
                return "Login Successfull";
            }else {
                return "Account status is InActive";
            }
        }
    }

    @Override
    public String forgotPwd(String email) {
        UserMaster byEmail = userMasterRepo.findByEmail(email);
        if(byEmail==null){
            return "Invalid email";
        }
//      ToDo : Send pwd to user in email
        String subject = "Forgot password";
        String filename = "RecoverPwdBody.txt";
        String body = readEmailBody(byEmail.getFullName(), byEmail.getPassword(), filename);
        boolean sendMail = emailUtils.sendMail(email, subject, body);
        if(sendMail){
            return "Password sent to your registered email";
        }
        return null;
    }
    private String generateRandomPwd(){
                String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
                String numbers = "0123456789";

                String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
                StringBuilder sb = new StringBuilder();
                Random random = new Random();

                int length = 6;
                for(int i = 0; i < length; i++) {
                    int index = random.nextInt(alphaNumeric.length());
                    char randomChar = alphaNumeric.charAt(index);
                    sb.append(randomChar);
                }
                return sb.toString();
    }
    public String readEmailBody(String fullname , String pwd, String filename){

        String mailBody = null;
        try{
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);


            StringBuffer buffer = new StringBuffer();
            String line = br.readLine();
            while (line!=null){
//                Process the data
                buffer.append(line);
                line = br.readLine();
            }
            br.close();
            mailBody = buffer.toString();
            mailBody = mailBody.replace("{FULLNAME}", fullname);
            mailBody = mailBody.replace("{TEMP-PWD}", pwd);
            mailBody = mailBody.replace("{pwd}", pwd);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mailBody;
    }
}
