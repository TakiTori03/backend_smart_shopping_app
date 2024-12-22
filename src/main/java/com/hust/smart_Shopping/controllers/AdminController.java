package com.hust.smart_Shopping.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.smart_Shopping.dtos.ApiResponse;
import com.hust.smart_Shopping.dtos.admin.AdminRequest;
import com.hust.smart_Shopping.dtos.admin.AdminUpdateRequest;
import com.hust.smart_Shopping.models.Category;
import com.hust.smart_Shopping.models.Unit;
import com.hust.smart_Shopping.services.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> createCategory(@ModelAttribute AdminRequest request) {
        return ResponseEntity.ok(ApiResponse.<Category>builder()
                .payload(adminService.createCategory(request.getName())).build());
    }

    @GetMapping("/category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.<List<Category>>builder()
                .payload(adminService.getAllCategories()).build());
    }

    @PutMapping(value = "/category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateCategory(@ModelAttribute AdminUpdateRequest request) {
        adminService.updateCategory(request.getOldName(), request.getNewName());
        return ResponseEntity.ok(ApiResponse.builder().build());
    }

    @DeleteMapping("/category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@RequestParam("name") String name) {
        adminService.deleteCategory(name);
        return ResponseEntity.ok(ApiResponse.builder().build());
    }

    @PostMapping("/unit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> createUnit(@ModelAttribute AdminRequest request) {
        return ResponseEntity.ok(ApiResponse.<Unit>builder()
                .payload(adminService.createUnit(request.getName())).build());
    }

    @GetMapping("/unit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> getAllUnits() {
        return ResponseEntity.ok(ApiResponse.<List<Unit>>builder()
                .payload(adminService.getAllUnits()).build());
    }

    @PutMapping(value = "/unit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateUnit(@ModelAttribute AdminUpdateRequest request) {
        adminService.updateUnit(request.getOldName(), request.getNewName());
        return ResponseEntity.ok(ApiResponse.builder().build());
    }

    @DeleteMapping("/unit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteUnit(@RequestParam("name") String name) {
        adminService.deleteUnit(name);
        return ResponseEntity.ok(ApiResponse.builder().build());
    }
}
