package back.ciriu.security;

import back.ciriu.entities.LoginEntity;
import back.ciriu.repositories.LoginJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginJpaRepository loginJpaRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user with username: " + username);
        LoginEntity userEntity = loginJpaRepository.getLoginEntityByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                getAuthorities(userEntity)
        );
    }
    private Collection<? extends GrantedAuthority> getAuthorities(LoginEntity user) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRol()));
    }
}
