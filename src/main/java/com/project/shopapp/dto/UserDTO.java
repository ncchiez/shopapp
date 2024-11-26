package com.project.shopapp.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @JsonProperty("fullname")
    @NotBlank(message = "NAME_REQUIRED")
    private String fullName;

    @JsonProperty("email")
    @NotBlank(message = "EMAIL_REQUIRED")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "PASSWORD_REQUIRED")
    private String password;

    @JsonProperty("retype_password")
    @NotBlank(message = "RETYPE_PASSWORD_REQUIRED")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

//    @JsonProperty("facebook_account_id")
//    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

//    @JsonProperty("role_name")
//    @NotNull(message = "ROLE_ID_REQUIRED")
//    private String role;
}
