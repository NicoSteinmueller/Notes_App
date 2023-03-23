package com.nicosteinmueller.notes_app.Security.service;


import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    var optionalUser = userRepository.findUserByEmail(s);
    if (optionalUser.isPresent()) {
      var user = optionalUser.get();
     return User.withUsername(user.getEmail()).password(user.getPassword()).authorities("USER").build();
    } else {
      throw new UsernameNotFoundException(String.format("Email[%s] not found", s));
    }
  }
}
