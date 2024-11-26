package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

//    @JsonProperty("is_active")
//    private boolean active;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

//    @JsonProperty("facebook_account_id")
//    private int facebookAccountId;

//    @JsonProperty("google_account_id")
//    private int googleAccountId;

    private Role role;
}
