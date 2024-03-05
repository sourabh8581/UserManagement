package com.ashokit.usermgmt.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "USER_MASTER")
@Data
public class UserMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String fullName;
    private String email;
    private Long mobile;
    private String gender;
    private LocalDate dob;
    private Long ssn;
    private String password;
    private String accStatus;
    @CreationTimestamp
    @Column(name = "create_date" , updatable = false)
    private LocalDate createdDate;
    @UpdateTimestamp
    @Column(name = "update_date", insertable = false)
    private LocalDate updatedDate;
    private String createdBy;
    private String updatedBy;
}
