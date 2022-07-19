package com.horse.business.impl;

import com.horse.business.service.RoleService;
import com.horse.data.collection.Role;
import com.horse.data.dto.role.RoleRequest;
import com.horse.data.dto.role.RoleResponse;
import com.horse.data.repository.role.RoleRepository;
import com.horse.exception.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {

        if (roleRepository.findRoleByCode(roleRequest.getCode()).isPresent() || roleRepository.findRoleByName(roleRequest.getName()).isPresent()) {
            throw new DataNotFoundException("Role is existed in database");
        } else {
            Role role = new Role(roleRequest.getCode(), roleRequest.getName(), new Date());
            Role createdRole = roleRepository.save(role);

            return new RoleResponse(createdRole.getId(), createdRole.getName(), createdRole.getCode()
                    , createdRole.getCreatedAt(), null);
        }
    }

    @Override
    public RoleResponse updateRole(String id, RoleRequest roleRequest) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Role is not found with id: " + id));

        if (roleRequest.getCode() != null && !roleRequest.getCode().equals(role.getCode())) {

            if (roleRepository.findRoleByCode(roleRequest.getCode()).isPresent()) {
                throw new DataNotFoundException("Code role is existed in database");
            } else {
                role.setCode(roleRequest.getCode());
            }
        }

        if (roleRequest.getName() != null && !roleRequest.getName().equals(role.getName())) {

            if (roleRepository.findRoleByName(roleRequest.getName()).isPresent()) {
                throw new DataNotFoundException("Name role is existed in database");
            } else {
                role.setCode(roleRequest.getName());
            }
        }

        role.setModifiedAt(new Date());

        Role modifiedRole = roleRepository.save(role);
        return new RoleResponse(modifiedRole.getId(), modifiedRole.getName(), modifiedRole.getCode()
                , modifiedRole.getCreatedAt(), modifiedRole.getModifiedAt());
    }

    @Override
    public void deleteRole(String id) {

       Role role = roleRepository.findById(id)
               .orElseThrow(() -> new DataNotFoundException("Role not found with id: " + id));
       roleRepository.delete(role);
    }

    @Override
    public RoleResponse getOne(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Role not found with id: " + id));

        return new RoleResponse(role.getId(), role.getName(), role.getCode(), role.getCreatedAt(), role.getModifiedAt());

    }

    @Override
    public Set<RoleResponse> getAll() {

        return roleRepository.findAll()
                .stream().map(role -> new RoleResponse(role.getId(), role.getName(), role.getCode(), role.getCreatedAt(), role.getModifiedAt()))
                .collect(Collectors.toSet());
    }
}
