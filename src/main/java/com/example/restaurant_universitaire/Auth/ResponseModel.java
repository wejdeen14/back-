package com.example.restaurant_universitaire.Auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ResponseModel {
    private String message;
    private int code;
    private HttpStatus status;
    private String username;
    private String password;
}
