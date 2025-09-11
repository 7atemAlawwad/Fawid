package com.example.finalprojectjavabootcamp.DTOIN;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SellerDTOIn {

    private String name;

    private String email;

    private String location;

    private String phone;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$", message = "Password must contain at least one letter, one number, and one special character (!@#$%^&*)")
    private String password;

    private String confirmPassword;

    private String description;

    private String preferredContact;
}
