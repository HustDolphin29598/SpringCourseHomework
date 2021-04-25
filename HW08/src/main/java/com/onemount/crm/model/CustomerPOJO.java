package com.onemount.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class CustomerPOJO {
    @Size(min = 2, max = 50, message = "Full name length must be between 2 and 50")
    private String fullname;

    @Email(message = "Email is not valid! Check it carefully !")
    private String email;

    @Pattern(regexp = "^\\d{10,11}$", message = "Mobile number must be numbers and between 10 and 11")
    @NotEmpty(message = "Mobile phone is required")
    private String mobile;
}
