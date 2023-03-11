package com.works.services;

import com.works.entities.Admin;
import com.works.entities.Role;
import com.works.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService implements UserDetailsService {

    final AdminRepository adminRepository;
    final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> optionalAdmin = adminRepository.findByUsernameEqualsIgnoreCase(username);
        if (optionalAdmin.isPresent()) {
            Admin adm = optionalAdmin.get();
            return new User(
                    adm.getUsername(),
                    adm.getPassword(),
                    adm.isEnable(),
                    true,
                    true,
                    true,
                    parseRole(adm.getRoles())
            );
        }else {
            throw new UsernameNotFoundException("Not Found");
        }
    }

    private Collection<? extends GrantedAuthority> parseRole(List<Role> roles) {
        List<GrantedAuthority> ls = new ArrayList<>();
        for ( Role role : roles ) {
            ls.add( new SimpleGrantedAuthority(role.getName()));
        }
        return ls;
    }

    public Admin register( Admin admin ) {
        admin.setPassword( passwordEncoder.encode(admin.getPassword()) );
        adminRepository.save(admin);
        return admin;
    }


}
