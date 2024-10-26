package com.sugaram.sevice.serviceImpl;

import com.sugaram.config.JwtProvider;
import com.sugaram.entity.User;
import com.sugaram.enums.Gender;
import com.sugaram.enums.Role;
import com.sugaram.payload.SignInDTO;
import com.sugaram.payload.UserDTO;
import com.sugaram.repository.UserRepository;
import com.sugaram.sevice.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final JwtProvider jwtProvider;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> login(SignInDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.email()).orElseThrow(RuntimeException::new);
        if (!passwordEncoder.matches(userDTO.password(),user.getPassword())){
            throw new RuntimeException("Wrong password or username");
        }
//        if (!user.getPassword().equals(userDTO.password())) {
//            throw new RuntimeException("Wrong password");
//        }
        String token = jwtProvider.generateToken(user);

//        byte[] encode = Base64.getEncoder().encode((user.getUsername() + ":" + user.getPassword()).getBytes());
        return ResponseEntity.ok().body(token);
    }

    @Override
    public ResponseEntity<?> signUp(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new RuntimeException("Username is already in use");
        }
        if (!(userDTO.gender().toUpperCase().equals(Gender.MALE.toString()) || userDTO.gender().toUpperCase().equals(Gender.FEMALE.toString()) ||userDTO.gender().toUpperCase().equals(Gender.OTHER.toString()))) throw new RuntimeException("Invalid gender!\nUser can not be created!");
        User user = User
                .builder()
                .email(userDTO.email())
                .password(passwordEncoder.encode(userDTO.password()))
                .firstName(userDTO.firstName())
                .lastName(userDTO.lastName())
                .age(userDTO.age())
                .gender(Gender.valueOf(userDTO.gender().toUpperCase()))
                .role(Role.USER)
                .isNonLocked(true)
                .enabled(true)
                .build();
        userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }
}
