package com.works.services;

import com.works.entities.Admin;
import com.works.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    final DB db;
    final AdminRepository adminRepository;
    final TinkEncDec tinkEncDec;
    final HttpServletRequest request;

    public boolean login(Admin admin) {
        try {
            // String sql = "select * from admin where email = '"+admin.getEmail()+"' and password = '"+admin.getPassword()+"'";
            // select * from admin where email = 'a@a.com' and password = '' or 1 = 1 --'
            // Statement st = db.dataSource().getConnection().createStatement();
            // ResultSet rs = st.executeQuery(sql);
            // return rs.next();

            String sql = "select * from admin where email = ? and password = ?";
            PreparedStatement st = db.dataSource().getConnection().prepareStatement(sql);
            st.setString(1, admin.getEmail());
            st.setString(2, admin.getPassword());
            ResultSet rs = st.executeQuery();
            return rs.next();

        }catch (Exception ex) {
            System.err.println("Login Error : " + ex);
        }
        return false;
    }


    public boolean jpaLogin( Admin admin ) {
        Optional<Admin> optionalAdmin = adminRepository.findByEmailEqualsIgnoreCase(admin.getEmail());
        if ( optionalAdmin.isPresent() ) {
            Admin adm = optionalAdmin.get();
            String newPass = tinkEncDec.decrypt(adm.getPassword());
            if ( newPass.equals( admin.getPassword() )) {
                request.getSession().setAttribute("admin", adm);
                return true;
            }
        }
        return false;
    }

    public void logout() {
        request.getSession().removeAttribute("admin");
    }


}
