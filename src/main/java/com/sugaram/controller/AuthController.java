package com.sugaram.controller;


import com.sugaram.config.JwtProvider;
import com.sugaram.entity.User;
import com.sugaram.payload.SignInDTO;
import com.sugaram.payload.UserDTO;
import com.sugaram.repository.UserRepository;
import com.sugaram.sevice.serviceImpl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    final JwtProvider jwtProvider;
    final UserRepository userRepository;
    final AuthServiceImpl authService;


    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO){

        return authService.signUp(userDTO);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInDTO userDTO){
        return authService.login(userDTO);
    }

    @PutMapping("/password")
    public ResponseEntity<?> updateUser(@RequestParam String email, @RequestParam String password){
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setPassword(password);
        userRepository.save(user);
        return ResponseEntity.ok("Password updated successfully");
    }
}
