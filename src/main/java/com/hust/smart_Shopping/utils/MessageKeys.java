package com.hust.smart_Shopping.utils;

public class MessageKeys {

    // auth
    public static final String LOGIN_FAILED = "auth.login.failed";
    public static final String LOGIN_SUCCESS = "auth.login.success";
    public static final String SEND_CODE_SUCCESS = "auth.send_code.success";
    public static final String VERIFY_SUCCESS = "auth.verify.success";
    public static final String CHANGE_PASS_SUCCESS = "auth.change_password.success";
    public static final String EDIT_PROFILE_SUCCESS = "auth.edit_profile.success";
    public static final String REGISTER_SUCCESS = "auth.register.success";
    public static final String GET_INFO_SUCCESS = "auth.get_info.success";
    public static final String DELETE_USER_SUCCESS = "auth.delete_user.success";

    // group
    public static final String CREATE_GROUP_SUCCESS = "group.create.success";
    public static final String SUCCESS = "group.success";
    public static final String DELETE_MEMBER_SUCCESS = "group.delete_member.success";
    public static final String GET_INFO_GROUP_MEMBER_SUCCESS = "group.get_info_member.success";

    // food
    public static final String CREATE_FOOD_SUCCESS = "food.create.success";
    public static final String DELETE_FOOD_SUCCESS = "food.delete.success";
    public static final String GET_ALL_FOOD_SUCCESS = "food.get_all.success";
    public static final String GET_UNIT_FOOD_SUCCESS = "food.get_unit.success";
    public static final String GET_CATEGORY_FOOD_SUCCESS = "food.get_category.success";

    // fridge
    public static final String CREATE_FRIDGE_ITEM_SUCCESS = "fridge.create_item.success";
    public static final String DELETE_FRIDGE_ITEM_SUCCESS = "fridge.delete_item.success";
    public static final String GET_ALL_FRIDGE_ITEM_SUCCESS = "fridge.get_all_items.success";
    public static final String GET_SPECIFIC_ITEM_SUCCESS = "fridge.get_specific_item.success";

    // shopping list
    public static final String CREATE_SHOPPINGLIST_FOR_MEMBER_SUCCESS = "shopping_list.create_for_member.success";
    public static final String DELETE_SHOPPINGLIST_SUCCESS = "shopping_list.delete.success";
    public static final String CREATE_TASK_SUCCESS = "shopping_list.create_task.success";
    public static final String GET_ALL_SHOPPINGLIST_SUCCESS = "shopping_list.get_all.success";
    public static final String DELETE_TASK_SUCCESS = "shopping_list.delete_task.success";

    // meal plan
    public static final String CREATE_MEAL_PLAN_SUCCESS = "meal_plan.create.success";
    public static final String DELETE_MEAL_PLAN_SUCCESS = "meal_plan.delete.success";
    public static final String GET_PLAN_BY_DATE_SUCCESS = "meal_plan.get_by_date.success";

    // recipe
    public static final String CREATE_RECIPE_SUCCESS = "recipe.create.success";
    public static final String DELETE_RECIPE_SUCCESS = "recipe.delete.success";
    public static final String GET_RECIPE_BY_FOOD_SUCCESS = "recipe.get_by_food.success";

    // business logic exception
    public static final String CATEGORY_EXIST = "exception.category.exist";
    public static final String UNIT_EXIST = "exception.unit.exist";
    public static final String FOOD_EXIST = "exception.food.exist";
    public static final String RECIPE_EXIST = "exception.recipe.exist";
    public static final String YOU_NOT_HAVE_PERMISSION = "exception.permission.denied";
    public static final String YOU_NOT_CREATE_FAMILY = "exception.family.not_created";
    public static final String YOU_CREATED_FAMILY = "exception.family.already_created";
    public static final String USER_HAVED_FAMILY = "exception.user.has_family";
    public static final String USER_NOT_HAVED_FAMILY = "exception.user.no_family";

    // register
    public static final String PASSWORD_NOT_MATCH = "register.password.not_match";

    public static final String TOKEN_EXPIRATION_TIME = "token.expiration.time";
    public static final String NOT_FOUND = "error.not_found";
    public static final String USER_EXISTED = "error.user.existed";

    // validation
    public static final String ERROR_MESSAGE = "validation.error";
    public static final String PHONE_NUMBER_REQUIRED = "validation.phone_number.required";
    public static final String PASSWORD_REQUIRED = "validation.password.required";
    public static final String ROLE_ID_REQUIRED = "validation.role_id.required";
    public static final String USER_ID_REQUIRED = "validation.user_id.required";
    public static final String PHONE_NUMBER_SIZE_REQUIRED = "validation.phone_number.size";
    public static final String CATEGORIES_NAME_REQUIRED = "validation.categories_name.required";
    public static final String USER_ID_LOCKED = "validation.user.locked";
    public static final String USER_ID_UNLOCKED = "validation.user.unlocked";
    public static final String REFRESH_TOKEN_SUCCESS = "token.refresh.success";

    public static final String EMAIL_EXISTED = "auth.email.existed";
    public static final String PHONE_NUMBER_AND_PASSWORD_FAILED = "auth.phone_number_password.failed";
    public static final String CAN_NOT_CREATE_ACCOUNT_ROLE_ADMIN = "auth.role.admin_creation_denied";
    public static final String USER_VERIFIED = "auth.user.verified";

    // token
    public static final String ERROR_REFRESH_TOKEN = "token.refresh.failed";
    public static final String FILES_REQUIRED = "file.required";
    public static final String FILES_IMAGES_SUCCESS = "file.images.success";
    public static final String FILES_IMAGES_FAILED = "file.images.failed";

    // error get
    public static final String MESSAGE_ERROR_GET = "error.get.failed";
    public static final String MESSAGE_UPDATE_GET = "update.success";
    public static final String MESSAGE_DELETE_SUCCESS = "delete.success";
    public static final String MESSAGE_DELETE_FAILED = "delete.failed";

    public static final String RESET_PASSWORD_SUCCESS = "password.reset.success";
    public static final String RESET_PASSWORD_FAILED = "password.reset.failed";
    public static final String BAD_LOGIC = "error.logic.bad";

    public static final String CREATE_CATEGORIES_SUCCESS = "category.create.success";
    public static final String CREATE_CATEGORIES_FAILED = "category.create.failed";
    public static final String UPDATE_CATEGORIES_SUCCESS = "category.update.success";
    public static final String UPDATE_CATEGORIES_FAILED = "category.update.failed";
    public static final String DELETE_CATEGORIES_SUCCESS = "category.delete.success";
    public static final String DELETE_CATEGORIES_FAILED = "category.delete.failed";

    public static final String CREATE_UNITS_SUCCESS = "unit.create.success";
    public static final String CREATE_UNITS_FAILED = "unit.create.failed";
    public static final String UPDATE_UNITS_SUCCESS = "unit.update.success";
    public static final String UPDATE_UNITS_FAILED = "unit.update.failed";
    public static final String DELETE_UNITS_SUCCESS = "unit.delete.success";
    public static final String DELETE_UNITS_FAILED = "unit.delete.failed";

    public static final String GET_INFORMATION_FAILED = "information.get.failed";
    public static final String GET_INFORMATION_SUCCESS = "information.get.success";

    public static final String APP_AUTHORIZATION_403 = "error.authorization.403";
    public static final String APP_UNCATEGORIZED_500 = "error.uncategorized.500";
    public static final String APP_PERMISSION_DENY_EXCEPTION = "error.permission.deny";
}