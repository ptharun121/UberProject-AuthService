package com.example.uberprojectauthservice.controllers;

import com.example.uberprojectauthservice.dto.AuthRequestDto;
import com.example.uberprojectauthservice.dto.AuthResponseDto;
import com.example.uberprojectauthservice.dto.PassengerSignupRequestDto;
import com.example.uberprojectauthservice.dto.PassengerSignupResponseDto;
import com.example.uberprojectauthservice.services.AuthService;
import com.example.uberprojectauthservice.services.JWTService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${cookie.expiry}")
    private int cookieExpiry;

    private AuthService authService;

    private AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager
                            , JWTService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerSignupResponseDto> signUp(@RequestBody PassengerSignupRequestDto passengerSignupRequestDto) {
        PassengerSignupResponseDto response = this.authService.signupPassenger(passengerSignupRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response) {
        System.out.println(authRequestDto);
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
        if (authentication.isAuthenticated()) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("email", authRequestDto.getEmail());
            String token = this.jwtService.createToken(authRequestDto.getEmail());
            ResponseCookie cookie = ResponseCookie.from("JWTToken", token)
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(cookieExpiry)
                            .build();
            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            //return new ResponseEntity<>(AuthResponseDto.builder().success(true).build(), HttpStatus.OK);
            return new ResponseEntity<>("DONE", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            System.out.println(cookie.getName()+ " ===== " + cookie.getValue());
        }
        System.out.println("validate token");
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

}
