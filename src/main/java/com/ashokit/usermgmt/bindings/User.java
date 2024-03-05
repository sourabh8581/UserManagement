package com.ashokit.usermgmt.bindings;

import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDate;


@Data
public class User {
    private String fullName;
    private String email;
    private Long mobile;
    private String gender;
    private LocalDate dob;
    private Long ssn;

}
