package com.iot.data_management_system.entities.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String role;
}
