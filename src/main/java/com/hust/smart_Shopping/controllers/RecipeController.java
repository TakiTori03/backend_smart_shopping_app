package com.hust.smart_Shopping.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.smart_Shopping.components.TranslateMessages;
import com.hust.smart_Shopping.dtos.ApiResponse;

import com.hust.smart_Shopping.dtos.recipe.AddRecipeRequest;
import com.hust.smart_Shopping.dtos.recipe.RecipeRequest;
import com.hust.smart_Shopping.dtos.recipe.RecipeResponse;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.services.RecipeService;
import com.hust.smart_Shopping.services.UserService;
import com.hust.smart_Shopping.utils.MessageKeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/recipe")
@RequiredArgsConstructor
public class RecipeController extends TranslateMessages {

        private final RecipeService recipeService;
        private final UserService userService;

        @PostMapping()
        public ResponseEntity<ApiResponse<?>> createRecipe(@ModelAttribute AddRecipeRequest request,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<RecipeResponse>builder()
                                .payload(RecipeResponse
                                                .fromRecipe(recipeService.createRecipe(request.getFoodName(),
                                                                request.getName(),
                                                                request.getHtmlContent(), request.getDescription(),
                                                                user)))
                                .message(translate(MessageKeys.CREATE_RECIPE_SUCCESS))
                                .build());
        }

        @PutMapping()
        public ResponseEntity<ApiResponse<?>> updateRecipe(@ModelAttribute RecipeRequest request,
                        @RequestHeader("Authorization") String token) {

                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<RecipeResponse>builder()
                                .payload(RecipeResponse.fromRecipe(recipeService.updateRecipe(request.getRecipeId(),
                                                request.getNewHtmlContent(),
                                                request.getNewDescription(), request.getNewFoodName(),
                                                request.getNewName(), user)))
                                .message(translate(MessageKeys.SUCCESS))
                                .build());
        }

        @DeleteMapping()
        public ResponseEntity<ApiResponse<?>> deleteFridgeItem(@RequestParam("recipeId") Long id,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                recipeService.deleteRecipe(id, user);
                return ResponseEntity.ok(
                                ApiResponse.builder().message(translate(MessageKeys.DELETE_RECIPE_SUCCESS)).build());
        }

        @GetMapping()
        public ResponseEntity<ApiResponse<?>> getRecipeFromFoodId(
                        @RequestParam("foodId") Long foodId) {

                return ResponseEntity.ok(ApiResponse.<List<RecipeResponse>>builder()
                                .payload(Optional.ofNullable(recipeService.getAllByFoodId(foodId))
                                                .orElse(Collections.emptyList())
                                                .stream()
                                                .map(recipe -> RecipeResponse.fromRecipe(recipe))
                                                .collect(Collectors.toList()))
                                .message(translate(MessageKeys.GET_RECIPE_BY_FOOD_SUCCESS))
                                .build());
        }
}
