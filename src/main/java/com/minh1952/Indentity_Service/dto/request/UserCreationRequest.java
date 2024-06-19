package com.minh1952.Indentity_Service.dto.request;

import jakarta.validation.constraints.Size;

public class UserCreationRequest {
    private String username;

    // set Validation cho trường thông tin của class làm việc trực tiếp với request từ tầng Service
    @Size(min = 8 , max = 15, message = "password must be at least 8-15 character")
    private String password;
    private String firstName;
    private String lastName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}
