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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.smart_Shopping.components.TranslateMessages;
import com.hust.smart_Shopping.dtos.ApiResponse;

import com.hust.smart_Shopping.dtos.meal.AddMealPlanRequest;
import com.hust.smart_Shopping.dtos.meal.MealPlanRequest;
import com.hust.smart_Shopping.dtos.meal.MealPlanResponse;

import com.hust.smart_Shopping.models.User;

import com.hust.smart_Shopping.services.MealPlanService;
import com.hust.smart_Shopping.services.UserService;
import com.hust.smart_Shopping.utils.MessageKeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/meal")
@RequiredArgsConstructor
public class MealPlanController extends TranslateMessages {

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
                                .message(translate(MessageKeys.CREATE_MEAL_PLAN_SUCCESS))
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
                                .message(translate(MessageKeys.SUCCESS))
                                .build());
        }

        @DeleteMapping()
        public ResponseEntity<ApiResponse<?>> deleteFridgeItem(@RequestParam("planId") Long id,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                mealPlanService.deletePlan(id, user);
                return ResponseEntity.ok(
                                ApiResponse.builder().message(translate(MessageKeys.DELETE_MEAL_PLAN_SUCCESS)).build());
        }

        @GetMapping()
        public ResponseEntity<ApiResponse<?>> getPlanByDate(
                        @RequestParam("date") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<List<MealPlanResponse>>builder()
                                .payload(Optional.ofNullable(mealPlanService.getAllByDate(date, user))
                                                .orElse(Collections.emptyList())
                                                .stream()
                                                .map(mealPlan -> MealPlanResponse.allFromMealPlan(mealPlan))
                                                .collect(Collectors.toList()))
                                .message(translate(MessageKeys.GET_PLAN_BY_DATE_SUCCESS))
                                .build());
        }
}
