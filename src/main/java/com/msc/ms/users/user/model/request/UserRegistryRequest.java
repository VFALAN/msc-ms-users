package com.msc.ms.users.user.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.msc.ms.users.user.validation.unique.email.UniqueEmail;
import com.msc.ms.users.user.validation.unique.phonenumber.UniquePhoneNumber;
import com.msc.ms.users.user.validation.unique.username.UniqueUsername;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistryRequest {
    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Last name is required")
    private String lastName;
    @NotEmpty(message = "Middle name is required")
    private String middleName;
    @NotEmpty(message = "Phone number required")
    @Pattern(regexp = "\\d{10,12}", message = "phone number format incorrect")
    @UniquePhoneNumber
    private String phoneNumber;
    @NotEmpty(message = "Username is required")
    @UniqueUsername
    private String userName;
    @NotNull
    @Min(value = 17, message = "Age required for any user is for less 17")
    private Integer age;
    @NotNull(message = "BirthDate is required")
    private Date birthDate;
    @NotEmpty(message = "Email is required")
    @Email(message = "Email format wrong")
    @UniqueEmail
    private String email;
    @NotBlank(message = "password is required")
    private String password;
}
