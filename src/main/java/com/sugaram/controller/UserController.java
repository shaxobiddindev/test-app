package com.sugaram.controller;

import com.sugaram.entity.User;
import com.sugaram.enums.Gender;
import com.sugaram.payload.EditUserDTO;
import com.sugaram.repository.UserRepository;
import com.sugaram.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class UserController {
    private final UserRepository userRepository;
    @GetMapping
    public ResponseEntity<?> profile() {
        return ResponseEntity.ok(Util.getCurrentUser());
    }
    @PutMapping
    private ResponseEntity<?> editUser(@RequestBody EditUserDTO userDTO){
        User user = Util.getCurrentUser();
        user.setAge(userDTO.age() == null?user.getAge():userDTO.age());
        user.setFirstName(userDTO.firstName() == null?user.getFirstName():userDTO.firstName());
        user.setLastName(userDTO.lastName() == null?user.getLastName():userDTO.lastName());
        user.setGender(userDTO.gender() == null?user.getGender(): Gender.valueOf(userDTO.gender().toUpperCase()));
        return ResponseEntity.ok(userRepository.save(user));
    }
}
