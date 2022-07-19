package com.horse.api;

import com.horse.business.service.AccountService;
import com.horse.data.dto.account.AccountResponse;
import com.horse.data.dto.account.PasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordRequest passwordRequest) {

        accountService.changePassword(passwordRequest.getPassword());
        return new ResponseEntity<>("Your password changes successfully!", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getInfo() {
        AccountResponse accountResponse = accountService.getInfo();
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id) {
        AccountResponse accountResponse = accountService.getOne(id);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }
}
