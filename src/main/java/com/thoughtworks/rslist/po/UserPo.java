package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "user")
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPo {
    @Id
    @GeneratedValue
    private  int id;
    @Column(name = "name")
    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    @Builder.Default
    private int voteNum = 10;


}
