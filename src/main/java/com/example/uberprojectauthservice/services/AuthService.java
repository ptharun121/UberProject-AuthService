package com.example.uberprojectauthservice.services;

import com.example.uberprojectauthservice.dto.PassengerSignupRequestDto;
import com.example.uberprojectauthservice.dto.PassengerSignupResponseDto;
import com.example.uberprojectauthservice.repositories.PassengerRepository;
import com.example.uberprojectentityservice.models.Passenger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private PassengerRepository passengerRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(PassengerRepository passengerRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.passengerRepository = passengerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public PassengerSignupResponseDto signupPassenger(PassengerSignupRequestDto passengerSignupRequestDto) {

        Passenger passenger = Passenger.builder()
                .email(passengerSignupRequestDto.getEmail())
                .name(passengerSignupRequestDto.getName())
                .phoneNumber(passengerSignupRequestDto.getPhoneNumber())
                .password(bCryptPasswordEncoder.encode(passengerSignupRequestDto.getPassword()))
                .build();
        this.passengerRepository.save(passenger);

        PassengerSignupResponseDto response = PassengerSignupResponseDto.from(passenger);
        return response;
    }
}
