package com.horse.business.service;

import com.horse.data.dto.role.RoleRequest;
import com.horse.data.dto.role.RoleResponse;

import java.util.Set;

public interface RoleService {

    RoleResponse createRole(RoleRequest roleRequest);
    RoleResponse updateRole(String id, RoleRequest roleRequest);
    void deleteRole(String id);
    RoleResponse getOne(String id);
    Set<RoleResponse> getAll();
}
