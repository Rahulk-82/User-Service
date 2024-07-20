package com.auth.user.services;

import com.auth.user.models.Token;
import com.auth.user.models.User;
import com.auth.user.repositories.TokenRepository;
import com.auth.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public User signUp(String name, String email, String password){

        // skipping email varification part done
        Optional<User> user=userRepository.findByEmail(email);
        if(!user.isPresent()){
            //throw exception

        }
        User user1=new User();
        user1.setEmail(email);
        user1.setName(name);
        user1.setHashedPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(user1);
        return user1;
    }

    public Token login(String email, String password){

        Optional<User> optionalUser=userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            //throw exception;
            return null;
        }
        User user=optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password,user.getHashedPassword())){
             //thorw excption
            return null;
        }
        Token token=new Token();
        token.setUser(user);
        token.setExpirydate(get30daysLaterDate());
        token.setValue(UUID.randomUUID().toString());


        return tokenRepository.save(token);
    }

    private Date get30daysLaterDate() {
        Date date=new Date();

        //convert date to calender
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);

        //add 30 days to it.
        calendar.add(Calendar.DAY_OF_MONTH,30);
        return calendar.getTime();
    }

    public void logout(String token) {
        // If the token is already deleted, no need to do anything.
        // If the token is already expired, no need to do anything.
        // else set deleted as true.

        Optional<Token> tokenOptional=tokenRepository.findByValueAndIsDeletedEquals(token,false); // check function, if token is present and is not deleted or expired.
        if(tokenOptional.isEmpty()){
            // throw an exception saying token is not present or already deleted.
            return ;
        }
        Token updatedToken=tokenOptional.get();
        updatedToken.setDeleted(true);
        tokenRepository.save(updatedToken);


    }

    public boolean validateToken(String token) {
        /*
            1. Check if the token is present in DB.
            2. Check if the token is not deleted.
            3. Check if the token is not expired.
         */

        Optional<Token> tokenOptional= tokenRepository.findByValueAndIsDeletedEqualsAndExpirydateGreaterThan(token,false,new Date());
        return tokenOptional.isPresent();
    }
}
