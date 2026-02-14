package com.example.phegon.phegonBank.role.controller;

import com.example.phegon.phegonBank.res.Response;
import com.example.phegon.phegonBank.role.entity.RoleEntity;
import com.example.phegon.phegonBank.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<Response<RoleEntity>> createRole(@RequestBody RoleEntity roleRequest){
        return ResponseEntity.ok(roleService.createRole(roleRequest));
    }

    @PutMapping
    public ResponseEntity<Response<RoleEntity>> updateRole(@RequestBody RoleEntity roleRequest){
        return ResponseEntity.ok(roleService.updateRole(roleRequest));
    }

    @GetMapping
    public ResponseEntity<Response<List<RoleEntity>>> getAllRoles(){
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteRole(@PathVariable Long id){
        return ResponseEntity.ok(roleService.deleteRole(id));
    }
}
