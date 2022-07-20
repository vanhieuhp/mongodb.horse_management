package com.horse.api;

import com.horse.business.service.AccountService;
import com.horse.config.token.AuthenticationRequest;
import com.horse.config.token.JwtTokenUtil;
import com.horse.data.dto.account.AccountRequest;
import com.horse.data.dto.account.AccountResponse;
import com.horse.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountRequest accountRequest) {

        AccountResponse accountResponse = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {

        // authenticate account
        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword());

            authenticationManager.authenticate(authentication);
        } catch (Exception e) {
            throw new UnauthorizedException("Username or password is incorrect");
        }

        String authority = accountService.getAuthoritiesOfAccount(authenticationRequest.getUsername());

        String accessToken = jwtTokenUtil.generateAccessToken(authenticationRequest.getUsername(), authority);
        String refreshToken = jwtTokenUtil.generateRefreshToken(authenticationRequest.getUsername());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String refreshToken = request.getHeader(AUTHORIZATION).substring("Bearer ".length());

        // check refreshToken
        if (jwtTokenUtil.checkFreshToken(refreshToken)) {

            // generate access Token
            String username = jwtTokenUtil.getSubjectFromToken(refreshToken);
            String authority = accountService.getAuthoritiesOfAccount(username);
            String accessToken = jwtTokenUtil.generateAccessToken(username, authority);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return new ResponseEntity<>(tokens, HttpStatus.OK);
        } else {
            UnauthorizedException.sendErrorTokenException(response, "Refresh Token is invalid");
            return null;
        }
    }
}
