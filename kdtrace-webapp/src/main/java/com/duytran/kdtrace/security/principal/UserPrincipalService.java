package com.duytran.kdtrace.security.principal;

import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserPrincipalService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Not Found:" + username)
        );
        return new UserPrincipal(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id){
        User user = userRepository.findUserById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id:" + id)
        );
        return new UserPrincipal(user);
    }

    public String getUserCurrentLogined(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public Long getUserIdCurrentLogined(){
        String username = getUserCurrentLogined();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RecordNotFoundException("User isn't exist")
        );
        return user.getId();
    }
}