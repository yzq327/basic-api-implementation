package com.thoughtworks.rslist.po;

import com.thoughtworks.rslist.domain.RsEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPo {
    @Id
    @GeneratedValue
    private  int id;
    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    @Builder.Default
    private int voteNum = 10;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userPo")
  //  @OneToMany(cascade = CascadeType.REMOVE)
    private List<RsEventPo> rsEventPos;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<VotePo> votePos;
}
