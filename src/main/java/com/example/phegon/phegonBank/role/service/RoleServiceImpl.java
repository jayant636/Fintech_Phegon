package com.example.phegon.phegonBank.role.service;

import com.example.phegon.phegonBank.exceptions.BadRequestException;
import com.example.phegon.phegonBank.exceptions.NotFoundException;
import com.example.phegon.phegonBank.res.Response;
import com.example.phegon.phegonBank.role.entity.RoleEntity;
import com.example.phegon.phegonBank.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Override
    public Response<RoleEntity> createRole(RoleEntity roleRequest) {
        if(roleRepository.findByName(roleRequest.getName()).isPresent()){
            throw new BadRequestException("Role already exists");
        }
        RoleEntity savedRole = roleRepository.save(roleRequest);
        return Response.<RoleEntity> builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role saved successfully")
                .data(savedRole)
                .build();
    }

    @Override
    public Response<RoleEntity> updateRole(RoleEntity roleRequest) {
        RoleEntity role = roleRepository.findById(roleRequest.getId()).orElseThrow(()-> new NotFoundException("Role not Found"));
        role.setName(roleRequest.getName());
        RoleEntity updateRole = roleRepository.save(role);

        return Response.<RoleEntity> builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role updated successfully")
                .data(updateRole)
                .build();

    }

    @Override
    public Response<List<RoleEntity>> getAllRoles() {
        List<RoleEntity> roles = roleRepository.findAll();

        return Response.<List<RoleEntity>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role updated successfully")
                .data(roles)
                .build();

    }

    @Override
    public Response<?> deleteRole(Long id) {

       if(!roleRepository.existsById(id)){
           throw new NotFoundException("Role Not Found");
       }

       roleRepository.deleteById(id);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role deleted successfully")
                .build();


    }
}
