package com.works.services;

import com.works.entities.Admin;
import com.works.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    final AdminRepository adminRepository;
    final PasswordEncoder passwordEncoder;

    public Admin register( Admin admin ) {
        admin.setPassword( passwordEncoder.encode(admin.getPassword()) );
        adminRepository.save(admin);
        return admin;
    }

}
