package edu.eci.labinfo.bookinglab.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.eci.labinfo.bookinglab.data.UserRepository;
import edu.eci.labinfo.bookinglab.model.UserEntity;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userService;

    public UserDetailsServiceImpl(UserRepository userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userService.findByUserName(username);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            Collection<? extends GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
                    .collect(Collectors.toSet());
            return new User(user.getUserName(), user.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

}
