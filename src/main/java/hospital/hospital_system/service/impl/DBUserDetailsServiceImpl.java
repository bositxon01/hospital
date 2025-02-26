package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.User;
import hospital.hospital_system.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DBUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public DBUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedFalse(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return optionalUser.get();
    }
}
