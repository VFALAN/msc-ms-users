package com.msc.ms.users.user.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO implements Serializable {
    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Last name is required")
    private String lastName;
    @NotEmpty(message = "Middle name is required")
    private String middleName;
    @NotEmpty(message = "Phone number required")
    @Pattern(regexp = "[0-9]{10,12}", message = "phone number format incorrect")
    private String phoneNumber;
    @NotEmpty(message = "Username is required")
    private String userName;
    @NotNull
    @Min(value = 17, message = "Age required for any user is for less 17")
    private Integer age;
    @NotNull(message = "BirthDate is required")
    private Date birthDate;
    @NotEmpty(message = "Email is required")
    @Email(message = "Email format wrong")
    private String email;

    @NotNull(message = "Location is required")
    private Integer idLocation;
    @NotEmpty(message = "Street is required")
    private String street;
    @NotEmpty(message = "Number is required")
    private String number;
    @NotEmpty(message = "description is required")
    private String description;

    @NotNull(message = "Profile is required")
    private Integer idProfile;
}
