package com.horse.config.token;

import com.horse.data.collection.Account;
import com.horse.data.repository.account.AccountRepository;
import com.horse.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String currentUrl = request.getServletPath();
        if (currentUrl.equals("/api/login") || currentUrl.equals("/api/register") || currentUrl.equals("/api/refreshToken")){
            filterChain.doFilter(request, response);
        } else {

            String bearerToken = request.getHeader(AUTHORIZATION);

            if (isEmpty(bearerToken) || !bearerToken.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
            }

            // get token and validate
            final String token = bearerToken.split(" ")[1].trim();
            if (!jwtTokenUtil.validate(token)) {
                UnauthorizedException.sendErrorUnauthorizedException(response);
                return;
            }

            // get user and set data into spring security context holder
            String username = jwtTokenUtil.getSubjectFromToken(token);
            Account account = accountRepository.findByUsername(username).orElse(null);

            String role = jwtTokenUtil.getAuthoritiesFromToken(token);
            if (role == null || account == null) {
                UnauthorizedException.sendErrorUnauthorizedException(response);
                return;
            }

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            authorities.add(authority);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(account, null,authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        }
    }
}
