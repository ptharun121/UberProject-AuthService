package com.example.uberprojectauthservice.dto;

import com.example.uberprojectentityservice.models.Passenger;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerSignupResponseDto {

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    private Date createdAt;

    public static PassengerSignupResponseDto from(Passenger passenger) {
        PassengerSignupResponseDto result = PassengerSignupResponseDto.builder()
                .email(passenger.getEmail())
                .name(passenger.getName())
                .password(passenger.getPassword())
                .phoneNumber(passenger.getPhoneNumber())
                .createdAt(passenger.getCreatedAt())
                .build();
        return result;
    }
}
