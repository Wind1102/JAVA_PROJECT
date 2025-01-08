package com.minhhieu.identity_service.dto.request;

import com.minhhieu.identity_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min =3 , message = "USERNAME_INVALID")
    String username;

    @Size(min = 3, max = 20, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;


    @DobConstraint(min=18, message = "INVALID_DOB")
    LocalDate dob;

}
