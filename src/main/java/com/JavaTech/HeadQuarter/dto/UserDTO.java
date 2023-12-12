package com.JavaTech.HeadQuarter.dto;

import com.JavaTech.HeadQuarter.model.Branch;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("username")
    private String username;

    @JsonProperty("branch")
    private Branch branch;

    @JsonProperty("role")
    private String roleOfUser;
}
