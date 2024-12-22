package com.hust.smart_Shopping.controllers;

import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.smart_Shopping.dtos.ApiResponse;
import com.hust.smart_Shopping.dtos.food.FoodRequest;
import com.hust.smart_Shopping.dtos.food.FoodResponse;
import com.hust.smart_Shopping.models.Category;
import com.hust.smart_Shopping.models.Unit;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.services.FoodService;
import com.hust.smart_Shopping.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/food")
@RequiredArgsConstructor
public class FoodController {
        private final FoodService foodService;
        private final UserService userService;

        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<?>> createFood(@ModelAttribute FoodRequest request,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<FoodResponse>builder()
                                .payload(FoodResponse.fromFood(foodService.createFood(request.getName(),
                                                request.getFoodCategoryName(), request.getUnitName(),
                                                request.getImage(), user)))
                                .build());
        }

        @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<?>> updateFood(@ModelAttribute FoodRequest request,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<FoodResponse>builder()
                                .payload(FoodResponse.fromFood(foodService.updateFood(request.getName(),
                                                request.getFoodCategoryName(), request.getUnitName(),
                                                request.getImage(), user)))
                                .build());
        }

        @DeleteMapping()
        public ResponseEntity<ApiResponse<?>> deleteUnit(@RequestParam("name") String name) {
                foodService.deleteFood(name);
                return ResponseEntity.ok(ApiResponse.builder().build());
        }

        @GetMapping
        public ResponseEntity<ApiResponse<?>> getAllFoods() {
                return ResponseEntity.ok(ApiResponse.<List<FoodResponse>>builder()
                                .payload(foodService.getAllFoods().stream().map(food -> FoodResponse.allFromFood(food))
                                                .collect(Collectors.toList()))
                                .build());
        }

        @GetMapping("/unit")
        public ResponseEntity<ApiResponse<?>> getAllFoodUnits() {
                return ResponseEntity.ok(ApiResponse.<Set<Unit>>builder()
                                .payload(foodService.getAllFoods().stream().map(food -> food.getUnit())
                                                .collect(Collectors.toSet()))
                                .build());
        }

        @GetMapping("/category")
        public ResponseEntity<ApiResponse<?>> getAllFoodCategories() {
                return ResponseEntity.ok(ApiResponse.<Set<Category>>builder()
                                .payload(foodService.getAllFoods().stream().map(food -> food.getCategory())
                                                .collect(Collectors.toSet()))
                                .build());
        }

}