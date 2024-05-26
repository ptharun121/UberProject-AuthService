package com.example.uberprojectauthservice.services;

import com.example.uberprojectauthservice.helpers.AuthPassengerDetails;
import com.example.uberprojectauthservice.repositories.PassengerRepository;
import com.example.uberprojectentityservice.models.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class is responsible for loading user in the form of UserDetails object for auth.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Passenger> passenger = passengerRepository.findPassengerByEmail(username);
        if (passenger.isPresent()) {
            return new AuthPassengerDetails(passenger.get());
        } else {
            throw new UsernameNotFoundException("No Data with the email " + username + " found");
        }
    }
}
