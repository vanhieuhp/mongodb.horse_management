package com.horse.api;

import com.horse.business.service.AccountService;
import com.horse.data.dto.account.AccountResponse;
import com.horse.data.dto.account.PageInfoRequest;
import com.horse.data.dto.account.PasswordRequest;
import com.horse.data.dto.trainer.HiredTrainerRequest;
import com.horse.data.dto.trainer.HiredTrainerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {
        AccountResponse accountResponse = accountService.getInfo();
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id) {
        AccountResponse accountResponse = accountService.getOne(id);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @GetMapping("/trainers")
    public ResponseEntity<?> getAllTrainers() {
        return new ResponseEntity<>(accountService.getAllTrainers(), HttpStatus.OK);
    }

    @PutMapping("/trainers")
    public ResponseEntity<?> hiredTrainer(@RequestBody @Valid HiredTrainerRequest hiredTrainerRequest) {

        HiredTrainerResponse hiredTrainerResponse = accountService.hiredTrainer(hiredTrainerRequest);
        return new ResponseEntity<>(hiredTrainerResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAll(HttpServletRequest request) {
        Integer pageNumber = null;
        Integer pageSize = null;
        String sortBy = null;
        if (request.getParameter("pageNumber") != null) {
            pageNumber = Integer.valueOf(request.getParameter("pageNumber"));
        }
        if (request.getParameter("pageSize") != null) {
            pageSize = Integer.valueOf(request.getParameter("pageSize"));
        }
        if (request.getParameter("sortBy") != null) {
            sortBy = (request.getParameter("sortBy"));
        }
        PageInfoRequest pageInfoRequest = PageInfoRequest.builder().pageNumber(pageNumber).pageSize(pageSize).sortBy(sortBy).build();
        return new ResponseEntity<>(accountService.findAll(pageInfoRequest), HttpStatus.OK);
    }
}
