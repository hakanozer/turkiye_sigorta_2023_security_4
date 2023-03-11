package com.works.configs;

import com.works.entities.Info;
import com.works.repositories.InfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class FilterConfig implements Filter {

    final InfoRepository repository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getRequestURI();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String agent = req.getHeader("user-agent");
        String sessionId = req.getSession().getId();
        long date = System.currentTimeMillis();

        Info i = new Info();
        i.setSessionId(sessionId);
        i.setRoles(auth.getAuthorities().toString());
        i.setUrl(url);
        i.setName(auth.getName());
        i.setDate(date);
        i.setAgent(agent);
        repository.save(i);

        chain.doFilter(req, res);
    }

}
