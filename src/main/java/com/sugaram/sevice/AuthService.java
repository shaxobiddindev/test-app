package com.sugaram.sevice;

import com.sugaram.payload.SignInDTO;
import com.sugaram.payload.UserDTO;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    ResponseEntity<?> login(SignInDTO signInDTO);
    ResponseEntity<?> signUp(UserDTO userDTO);
}
