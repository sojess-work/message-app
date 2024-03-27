package com.org.messageapp.service;

import com.org.messageapp.dto.request.RegisterRequest;
import com.org.messageapp.dto.response.MappResponse;
import com.org.messageapp.entity.User;
import com.org.messageapp.exceptions.OperationFailedException;
import com.org.messageapp.exceptions.UserAlreadyExistsException;
import com.org.messageapp.exceptions.UserNotFoundException;
import com.org.messageapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class RegistrationService {
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Transactional
    public MappResponse register(RegisterRequest userDetails) throws Exception {
        User savedUser;
        var user = User.builder()
                .email(userDetails.getEmail())
                .password(passwordEncoder.encode(userDetails.getPassword()))
                .createdDate(LocalDate.now())
                .build();
            Optional<User> existUser = userRepository.findByEmail(userDetails.getEmail());
            if(existUser.isEmpty()) {
                 savedUser = userRepository.save(user);
                if (savedUser == null) {
                    throw new OperationFailedException("Failed Saving User");
                }
            }else{
                throw new UserAlreadyExistsException();
            }
        String jwtToken = jwtService.generateToken(user);
        return MappResponse.builder()
                .token(jwtToken)
                .build();

    }

    public MappResponse authenticate(RegisterRequest request) throws Exception {

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if(userOpt.isEmpty()){
            throw new UserNotFoundException();
        }else{
            User user = userOpt.get();
            if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
                var jwtToken = jwtService.generateToken(user);
                return MappResponse.builder()
                        .token(jwtToken)
                        .build();
            }else{
                throw  new BadCredentialsException("Invalid password");
            }
        }
    }

}
