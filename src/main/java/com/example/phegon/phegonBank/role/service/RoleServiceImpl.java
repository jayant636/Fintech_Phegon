package com.example.phegon.phegonBank.role.service;

import com.example.phegon.phegonBank.res.Response;
import com.example.phegon.phegonBank.role.entity.RoleEntity;
import com.example.phegon.phegonBank.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Override
    public Response<RoleEntity> createRole(RoleEntity roleRequest) {
        return null;
    }

    @Override
    public Response<RoleEntity> updateRole(RoleEntity roleRequest) {
        return null;
    }

    @Override
    public Response<List<RoleEntity>> getAllRoles() {
        return null;
    }

    @Override
    public Response<RoleEntity> deleteRole(Long id) {
        return null;
    }
}
