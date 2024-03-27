package com.org.messageapp.controller;

import com.org.messageapp.constants.MessageAppConstants;
import com.org.messageapp.dto.request.RegisterRequest;
import com.org.messageapp.dto.response.MappResponse;
import com.org.messageapp.exceptions.OperationFailedException;
import com.org.messageapp.exceptions.UserAlreadyExistsException;
import com.org.messageapp.exceptions.UserNotFoundException;
import com.org.messageapp.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/create-user")
    public ResponseEntity<MappResponse> createUser(@RequestBody  RegisterRequest user){
        log.info("POST to /api/auth/create-user --STARTS");
        try {
            MappResponse response = registrationService.register(user);

            log.info("POST to /api/auth/create-user --ENDS");
            return ResponseEntity.status(200).body(response);
        }catch (UserAlreadyExistsException e){

            log.debug("User with email {} already exists",user.getEmail());

            return ResponseEntity.status(400).body(
                    MappResponse.builder().message(
                            MessageAppConstants.EMAIL_ALREADY_EXISTS_MESSAGE
                    ).build());
        }
        catch (OperationFailedException e){

            log.error("Unable to save user for email id  {}",user.getEmail());

            return ResponseEntity.status(500).body(MappResponse.builder()
                    .message(MessageAppConstants.COULD_NOT_PROCESS_REQUEST_MESSAGE).build());
        }catch (Exception e){
            e.printStackTrace();
            log.error("Internal server error {}",e.getMessage());

            return ResponseEntity.status(500).body(MappResponse.builder()
                    .message(MessageAppConstants.COULD_NOT_PROCESS_REQUEST_MESSAGE).build());
        }
    }
    @GetMapping("/log-in")
    public ResponseEntity<MappResponse> login(@RequestBody RegisterRequest request){
        try{
            log.info("POST /api/auth/log-in --> STARTS");

            MappResponse response = registrationService.authenticate(request);

            log.info("POST /api/auth/log-in --> ENDS");
            return ResponseEntity.status(200).body(response);
        }catch (UserNotFoundException e){

            log.debug("User with email {} doesn't exists", request.getEmail());

            return ResponseEntity.status(400).body(MappResponse.builder()
                    .message(MessageAppConstants.EMAIL_DOESNT_EXISTS_MESSAGE).build());
        }catch (DisabledException e){

            log.debug("User disabled for email {}",request.getEmail());

            return ResponseEntity.status(401).body(MappResponse.builder()
                    .message(MessageAppConstants.EMAIL_DISABLED).build());
        }catch (BadCredentialsException e){

            log.debug("Bad credentials entered by user {}",request.getEmail());

            return ResponseEntity.status(401).body(MappResponse.builder()
                    .message(MessageAppConstants.BAD_CREDENTIALS).build());
        }catch (Exception e){
            e.printStackTrace();
            log.error("Internal server error {}",e.getMessage());

            return ResponseEntity.status(500).body(MappResponse.builder()
                    .message(MessageAppConstants.COULD_NOT_PROCESS_REQUEST_MESSAGE).build());
        }
    }

}
