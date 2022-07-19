package com.horse.api;

import com.horse.business.service.RoleService;
import com.horse.data.dto.role.RoleRequest;
import com.horse.data.dto.role.RoleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody @Valid RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.createRole(roleRequest);
        return new ResponseEntity<>(roleResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable("id") String id, @RequestBody RoleRequest roleRequest) {

        RoleResponse roleResponse = roleService.updateRole(id, roleRequest);
        return new ResponseEntity<>(roleResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {

        roleService.deleteRole(id);
        return new ResponseEntity<>("Delete role successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id) {
        RoleResponse roleResponse = roleService.getOne(id);
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        Set<RoleResponse> roles = roleService.getAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
