package com.hust.smart_Shopping.dtos.shopping_list;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hust.smart_Shopping.models.ShoppingList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingListResponse {
        private Long id;
        private String name;
        private String note;
        private Long beLongsToGroupAdminId;
        private Long assignedtoUserId;
        @DateTimeFormat(pattern = "MM/dd/yyyy")
        private LocalDate date;
        private Instant createdAt;
        private Instant updatedAt;
        private Long userId;
        private String userName;
        private List<TaskResponse> details;

        public static ShoppingListResponse fromShoppingList(ShoppingList shoppingList) {
                return ShoppingListResponse.builder()
                                .id(shoppingList.getId())
                                .name(shoppingList.getName())
                                .note(shoppingList.getNote())
                                .date(shoppingList.getDate())
                                .createdAt(shoppingList.getCreatedAt())
                                .updatedAt(shoppingList.getUpdatedAt())
                                .beLongsToGroupAdminId(
                                                shoppingList.getFamily() != null
                                                                ? shoppingList.getFamily().getLeader().getId()
                                                                : null)
                                .assignedtoUserId(shoppingList.getUser().getId())
                                .userId(shoppingList.getFamily() != null ? shoppingList.getFamily().getLeader().getId()
                                                : shoppingList.getUser().getId())
                                .build();

        }

        public static ShoppingListResponse allFromShoppingList(ShoppingList shoppingList) {
                return ShoppingListResponse.builder()
                                .id(shoppingList.getId())
                                .name(shoppingList.getName())
                                .note(shoppingList.getNote())
                                .date(shoppingList.getDate())
                                .createdAt(shoppingList.getCreatedAt())
                                .updatedAt(shoppingList.getUpdatedAt())
                                .beLongsToGroupAdminId(
                                                shoppingList.getFamily() != null
                                                                ? shoppingList.getFamily().getLeader().getId()
                                                                : null)
                                .assignedtoUserId(shoppingList.getUser().getId())
                                .userId(shoppingList.getFamily() != null ? shoppingList.getFamily().getLeader().getId()
                                                : shoppingList.getUser().getId())
                                .details(shoppingList.getTasks().stream().map(task -> TaskResponse.fromTask(task))
                                                .collect(Collectors.toList()))
                                .build();

        }
}
