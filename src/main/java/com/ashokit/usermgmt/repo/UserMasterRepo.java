package com.ashokit.usermgmt.repo;

import com.ashokit.usermgmt.entities.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;

public interface UserMasterRepo extends JpaRepository<UserMaster , Integer> {
    public UserMaster findByEmailAndPassword(String email , String pwd);
    public UserMaster findByEmail(String email);
}
