package com.hust.smart_Shopping.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hust.smart_Shopping.components.TranslateMessages;
import com.hust.smart_Shopping.dtos.ApiResponse;
import com.hust.smart_Shopping.dtos.shopping_list.AddShoppingListRequest;
import com.hust.smart_Shopping.dtos.shopping_list.AddTasksRequest;
import com.hust.smart_Shopping.dtos.shopping_list.ShoppingListRequest;
import com.hust.smart_Shopping.dtos.shopping_list.ShoppingListResponse;
import com.hust.smart_Shopping.dtos.shopping_list.TaskRequest;
import com.hust.smart_Shopping.exceptions.payload.BusinessLogicException;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.models.UserFamily;
import com.hust.smart_Shopping.repositories.UserFamilyRepository;
import com.hust.smart_Shopping.services.ShoppingListService;
import com.hust.smart_Shopping.services.UserService;
import com.hust.smart_Shopping.utils.MessageKeys;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/shopping")
@RequiredArgsConstructor
public class ShoppingListController extends TranslateMessages {

        private final UserService userService;
        private final ShoppingListService shoppingListService;
        private final UserFamilyRepository userFamilyRepository;

        @PostMapping()
        public ResponseEntity<ApiResponse<?>> createShoppingListInFamily(@ModelAttribute AddShoppingListRequest request,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<ShoppingListResponse>builder()
                                .payload(ShoppingListResponse
                                                .fromShoppingList(shoppingListService.createShoppingListInFamily(
                                                                request.getName(), request.getAssignToUsername(),
                                                                request.getNote(), request.getDate(), user)))
                                .message(translate(MessageKeys.CREATE_SHOPPINGLIST_FOR_MEMBER_SUCCESS))
                                .build());
        }

        @PutMapping()
        public ResponseEntity<ApiResponse<?>> updateShoppingList(@ModelAttribute ShoppingListRequest request,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<ShoppingListResponse>builder()
                                .payload(ShoppingListResponse.fromShoppingList(shoppingListService.updateShoppingList(
                                                request.getListId(), request.getNewName(),
                                                request.getNewAssignToUsername(),
                                                request.getNewDate(), request.getNewNote(), user)))
                                .message(translate(MessageKeys.SUCCESS))
                                .build());
        }

        @DeleteMapping()
        public ResponseEntity<ApiResponse<?>> deleteShoppingList(@PathParam("listId") Long listId,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                shoppingListService.deleteShoppingList(listId, user);
                return ResponseEntity.ok(ApiResponse.builder()
                                .message(translate(MessageKeys.DELETE_SHOPPINGLIST_SUCCESS))
                                .build());
        }

        @PostMapping("/task")
        public ResponseEntity<ApiResponse<?>> createTasks(@RequestBody AddTasksRequest request,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                shoppingListService.createTasksForList(request.getListId(), request.getTasks(), user);
                return ResponseEntity.ok(ApiResponse.builder()
                                .message(translate(MessageKeys.CREATE_TASK_SUCCESS))
                                .build());
        }

        @GetMapping("/task")
        public ResponseEntity<ApiResponse<?>> getTasksInFamily(
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                UserFamily userFamily = userFamilyRepository.findByUser(user)
                                .orElseThrow(() -> new BusinessLogicException(""));
                return ResponseEntity.ok(ApiResponse.<List<ShoppingListResponse>>builder()
                                .role(userFamily.getRole().getName())
                                .payload(shoppingListService.getListOfTaskInFamily(user).stream()
                                                .map(shoppingList -> ShoppingListResponse
                                                                .allFromShoppingList(shoppingList))
                                                .collect(Collectors.toList()))
                                .message(translate(MessageKeys.GET_ALL_SHOPPINGLIST_SUCCESS))
                                .build());
        }

        @DeleteMapping("/task")
        public ResponseEntity<ApiResponse<?>> deleteTaskInFamily(@PathParam("taskId") Long taskId,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                shoppingListService.deleteTaskInFamily(taskId, user);
                return ResponseEntity.ok(ApiResponse.builder()
                                .message(translate(MessageKeys.DELETE_TASK_SUCCESS))
                                .build());
        }

        @PutMapping("/task")
        public ResponseEntity<ApiResponse<?>> updateTaskInFamily(@ModelAttribute TaskRequest request,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                shoppingListService.updateTaskInFamily(request.getTaskId(), request.getNewFoodName(),
                                request.getQuantity(), user);
                return ResponseEntity.ok(ApiResponse.builder()
                                .message(translate(MessageKeys.SUCCESS))
                                .build());
        }

        @PutMapping("/task/mark")
        public void markTask(@PathParam("taskId") Long taskId,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                shoppingListService.markTask(taskId, user);

        }
}
