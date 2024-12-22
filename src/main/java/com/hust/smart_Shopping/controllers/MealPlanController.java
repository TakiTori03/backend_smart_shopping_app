package com.hust.smart_Shopping.controllers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.smart_Shopping.dtos.ApiResponse;
import com.hust.smart_Shopping.dtos.fridge.AddFridgeItemRequest;
import com.hust.smart_Shopping.dtos.fridge.FridgeItemResponse;
import com.hust.smart_Shopping.dtos.fridge.FridgeRequest;
import com.hust.smart_Shopping.dtos.meal.AddMealPlanRequest;
import com.hust.smart_Shopping.dtos.meal.MealPlanRequest;
import com.hust.smart_Shopping.dtos.meal.MealPlanResponse;
import com.hust.smart_Shopping.models.MealPlan;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.repositories.MealPlanRepository;
import com.hust.smart_Shopping.services.MealPlanService;
import com.hust.smart_Shopping.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/meal")
@RequiredArgsConstructor
public class MealPlanController {

        private final UserService userService;
        private final MealPlanService mealPlanService;

        @PostMapping()
        public ResponseEntity<ApiResponse<?>> createMealPlan(@ModelAttribute AddMealPlanRequest request,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<MealPlanResponse>builder()
                                .payload(MealPlanResponse
                                                .fromMealPlan(mealPlanService.createMealPlan(request.getFoodName(),
                                                                request.getTimestamp(),
                                                                request.getName(), user)))
                                .build());
        }

        @PutMapping()
        public ResponseEntity<ApiResponse<?>> updateMealPlan(@ModelAttribute MealPlanRequest request,
                        @RequestHeader("Authorization") String token) {

                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<MealPlanResponse>builder()
                                .payload(MealPlanResponse.fromMealPlan(mealPlanService.updateMealPlan(
                                                request.getPlanId(), request.getNewFoodName(),
                                                request.getNewName(), user)))
                                .build());
        }

        @DeleteMapping()
        public ResponseEntity<ApiResponse<?>> deleteFridgeItem(@RequestParam("planId") Long id,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                mealPlanService.deletePlan(id, user);
                return ResponseEntity.ok(ApiResponse.builder().build());
        }

        @GetMapping("{date}")
        public ResponseEntity<ApiResponse<?>> getPlanByDate(
                        @PathVariable("date") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<List<MealPlanResponse>>builder()
                                .payload(Optional.ofNullable(mealPlanService.getAllByDate(date, user))
                                                .orElse(Collections.emptyList())
                                                .stream()
                                                .map(mealPlan -> MealPlanResponse.allFromMealPlan(mealPlan))
                                                .collect(Collectors.toList()))
                                .build());
        }
}
