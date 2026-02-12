package com.example.phegon.phegonBank.role.service;


import com.example.phegon.phegonBank.res.Response;
import com.example.phegon.phegonBank.role.entity.RoleEntity;

import java.util.List;

public interface RoleService {
    Response<RoleEntity> createRole(RoleEntity roleRequest);
    Response<RoleEntity> updateRole(RoleEntity roleRequest);
    Response<List<RoleEntity>> getAllRoles();
    Response<RoleEntity> deleteRole(Long id);

}
