package com.ashokit.usermgmt.rest;

import com.ashokit.usermgmt.bindings.ActivateAccount;
import com.ashokit.usermgmt.bindings.Login;
import com.ashokit.usermgmt.bindings.User;
import com.ashokit.usermgmt.service.UserMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserRestController {

    @Autowired
    private UserMgmtService userMgmtService;
    @PostMapping("/user")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        boolean saveUser = userMgmtService.saveUser(user);
        if(saveUser){
            return new ResponseEntity<>("Registration Successfull", HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("Registration Failed" , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/activate")
    public ResponseEntity<String> activateAcc(@RequestBody ActivateAccount activateAccount){
        boolean activated = userMgmtService.activateUserAccount(activateAccount);
        if(activated){
            return new ResponseEntity<>("Account activated", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Invalid Temp Password", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> allUsers = userMgmtService.getAllUsers();
        return new ResponseEntity<>(allUsers , HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId){
        User userById = userMgmtService.getUserById(userId);
        return new ResponseEntity<>(userById , HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId ){
        boolean deleted = userMgmtService.deleteUserById(userId);
        if(deleted){
            return new ResponseEntity<>("User deleted" , HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/status/{userId}/{status}")
    public ResponseEntity<String> changeStatus(@PathVariable Integer userId , @PathVariable String status){
        boolean changed = userMgmtService.changeAccountStatus(userId, status);
        if(changed){
            return new ResponseEntity<>("Status Changed ", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Failed to change status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody Login login){
        String status = userMgmtService.login(login);
        return new ResponseEntity<>(status , HttpStatus.OK);
    }
    @GetMapping("/forgotPwd/{email}")
    public ResponseEntity<String> forgotPwd(@PathVariable String email){
        String status = userMgmtService.forgotPwd(email);
        return new ResponseEntity<>(status , HttpStatus.OK);
    }
}







