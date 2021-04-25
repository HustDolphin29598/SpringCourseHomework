package com.onemount.crm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity(name = "customer")
@Table(name = "customer")
public class Customer {
  @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Size(min = 2, max = 50, message = "full name length must be between 2 and 50")
  private String fullname;

  @Email
  private String email;

  @Pattern(regexp = "^\\d{10,11}$", message = "mobile number must be between 10 and 11")
  private String mobile;

}
