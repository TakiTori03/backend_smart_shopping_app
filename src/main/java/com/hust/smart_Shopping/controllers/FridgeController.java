package com.hust.smart_Shopping.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
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
import com.hust.smart_Shopping.dtos.food.FoodResponse;
import com.hust.smart_Shopping.dtos.fridge.AddFridgeItemRequest;
import com.hust.smart_Shopping.dtos.fridge.FridgeItemResponse;
import com.hust.smart_Shopping.dtos.fridge.FridgeRequest;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.services.FridgeService;
import com.hust.smart_Shopping.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/fridge")
@RequiredArgsConstructor
public class FridgeController {

        private final FridgeService fridgeService;
        private final UserService userService;

        @PostMapping()
        public ResponseEntity<ApiResponse<?>> createFridgeItem(@ModelAttribute AddFridgeItemRequest request,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<FridgeItemResponse>builder()
                                .payload(FridgeItemResponse
                                                .fromFridgeItem(fridgeService.createFridgeItem(request.getFoodName(),
                                                                request.getUseWithin(), request.getQuantity(), user)))
                                .build());
        }

        @PutMapping()
        public ResponseEntity<ApiResponse<?>> updateFridgeItem(@ModelAttribute FridgeRequest request) {

                return ResponseEntity.ok(ApiResponse.<FridgeItemResponse>builder()
                                .payload(FridgeItemResponse.fromFridgeItem(fridgeService.updateFridgeItem(
                                                request.getItemId(), request.getNewNote(), request.getNewQuantity(),
                                                request.getNewUseWithin(), request.getNewFoodName())))
                                .build());
        }

        @DeleteMapping()
        public ResponseEntity<ApiResponse<?>> deleteFridgeItem(@RequestParam("foodName") String foodName,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                fridgeService.deleteFridgeItem(foodName, user);
                return ResponseEntity.ok(ApiResponse.builder().build());
        }

        @GetMapping()
        public ResponseEntity<ApiResponse<?>> getAllFridgeItemOfYou(@RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<List<FridgeItemResponse>>builder()
                                .payload(fridgeService.getAllFridgeItemsWithUser(user).stream()
                                                .map(fridgeItem -> FridgeItemResponse.allFromFridgeItem(fridgeItem))
                                                .collect(Collectors.toList()))
                                .build());
        }

        @GetMapping("{foodName}")
        public ResponseEntity<ApiResponse<?>> getSpecificItem(@PathVariable("foodName") String foodName) {

                return ResponseEntity.ok(ApiResponse.<List<FridgeItemResponse>>builder()
                                .payload(fridgeService.getSpecificItem(foodName).stream()
                                                .map(fridgeItem -> FridgeItemResponse
                                                                .specificFromFridgeItem(fridgeItem))
                                                .collect(Collectors.toList()))
                                .build());
        }

}
