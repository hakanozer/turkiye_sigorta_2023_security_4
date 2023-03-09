package com.works.configs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.works.entities.Admin;
import lombok.RequiredArgsConstructor;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig implements Filter {

    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
        System.out.println( "Server Up" );
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        request.setCharacterEncoding("UTF8");
        response.setCharacterEncoding("UTF8");

        boolean errorStatus = false;
        String url = request.getRequestURI();

        // xss control
        long start = System.currentTimeMillis();
        try {

            Policy policy = Policy.getInstance(FilterConfig.class.getResourceAsStream("/antisamy-slashdot-1.4.4.xml"));
            AntiSamy sanitizer = new AntiSamy(policy);

            Map m = req.getParameterMap();
            Set s = m.entrySet();
            Iterator it = s.iterator();
            label:
            while (it.hasNext()) {

                Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();
                String key = entry.getKey();
                String[] value = entry.getValue();
                if (value.length > 0) {
                    for (int i = 0; i < value.length; i++) {
                        System.out.println("val : " + value[i].toString());
                        CleanResults scanned = sanitizer.scan(value[i].toString());
                        int errors = scanned.getNumberOfErrors(); // Kural ihlali sayısı
                        List<String> errorMsg = scanned.getErrorMessages(); // İhlal nedenleri
                        String sanitized = scanned.getCleanHTML(); // Temizlenmiş çıktı
                        System.out.println("Temiz çıktı: " + sanitized);
                        System.out.println("İhlal sayısı: " + errors);
                        System.out.println("İhlal nedenleri: " + errorMsg);
                        String ipAddress = request.getHeader("X-FORWARDED-FOR");
                        if (ipAddress == null) {
                            ipAddress = request.getRemoteAddr();
                            System.out.println("ip Address : " + ipAddress);
                        }
                        Map<String, String> hmHeader = getRequestHeadersInMap(request);
                        System.out.println("All Header : " + hmHeader);
                        System.out.println("Tarayıcı : " + hmHeader.get("user-agent"));
                        System.out.println("Lang : " + request.getLocalName());

                        if (errors > 0) {
                            //response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                            //response.setHeader("Location", "/xssError");
                            response.sendRedirect("/xssError");
                            errorStatus = true;
                            break label;
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("req hatası : " + e);
        }
        long end = System.currentTimeMillis();
        long between = end - start;
        System.out.println(between);

        if ( !errorStatus )
        chain.doFilter(request, response);
    }


    private Map<String, String> getRequestHeadersInMap(HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            result.put(key, value);
        }
        return result;
    }


    @Override
    public void destroy() {
        System.out.println("Server Down");
    }

}