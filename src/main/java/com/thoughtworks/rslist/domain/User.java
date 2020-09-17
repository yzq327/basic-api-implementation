package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;

import javax.validation.constraints.*;

public class User {
    @NotNull
    @Size(max = 8)
    private String name;
    @NotNull
    private String gender;
    @Min(18)
    @Max(100)
    private int age;
    @Email
    private String email;
    @Pattern(regexp = "1\\d{10}")
    private String phone;
    private int voteNum = 10;

    public User(String name, String gender, int age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    @JsonProperty(value="user-name")
    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty(value="user-gender")
    public void setGender(String gender) {
        this.gender = gender;
    }
    @JsonProperty(value="user-age")
    public void setAge(int age) {
        this.age = age;
    }
    @JsonProperty(value="user-email")
    public void setEmail(String email) {
        this.email = email;
    }
    @JsonProperty(value="user-phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }
    @JsonProperty(value="user-name")
    public String getName() {
        return name;
    }
    @JsonProperty(value="user-gender")
    public String getGender() {
        return gender;
    }
    @JsonProperty(value="user-age")
    public int getAge() {
        return age;
    }
    @JsonProperty(value="user-email")
    public String getEmail() {
        return email;
    }
    @JsonProperty(value="user-phone")
    public String getPhone() {
        return phone;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
