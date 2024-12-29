package com.hust.smart_Shopping.controllers;

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
import com.hust.smart_Shopping.dtos.user.GroupRequest;
import com.hust.smart_Shopping.dtos.user.CreateGroupResponse;
import com.hust.smart_Shopping.dtos.user.EditUserRequest;
import com.hust.smart_Shopping.dtos.user.FamilyInfoResponse;
import com.hust.smart_Shopping.dtos.user.LoginRequest;
import com.hust.smart_Shopping.dtos.user.LoginResponse;
import com.hust.smart_Shopping.dtos.user.PasswordChangeRequest;
import com.hust.smart_Shopping.dtos.user.RefreshTokenRequest;
import com.hust.smart_Shopping.dtos.user.RefreshTokenResponse;
import com.hust.smart_Shopping.dtos.user.RegistrationResponse;
import com.hust.smart_Shopping.dtos.user.VerificationRequest;
import com.hust.smart_Shopping.dtos.user.VerificationResponse;
import com.hust.smart_Shopping.dtos.user.UserRequest;
import com.hust.smart_Shopping.dtos.user.UserResponse;
import com.hust.smart_Shopping.models.Token;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.services.TokenService;
import com.hust.smart_Shopping.services.UserService;
import com.hust.smart_Shopping.utils.MessageKeys;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/user")
@RequiredArgsConstructor
public class UserController extends TranslateMessages {

        private final UserService userService;
        private final TokenService tokenService;

        // controller cho viec authentication
        @PostMapping()
        public ResponseEntity<ApiResponse<?>> register(@ModelAttribute @Valid UserRequest userRequest) {
                return ResponseEntity.ok().body(
                                ApiResponse.<RegistrationResponse>builder()

                                                .message(translate(MessageKeys.REGISTER_SUCCESS))
                                                .payload(userService.createUser(userRequest))
                                                .build());

        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<?>> login(@ModelAttribute @Valid LoginRequest loginRequest,
                        HttpServletRequest request) {

                String jwt = userService.login(
                                loginRequest.getEmail(),
                                loginRequest.getPassword());

                // check is mobile or web login
                String userAgent = request.getHeader("User-Agent");
                User user = userService.getUserDetailsFromToken(jwt);
                Token token = tokenService.addTokenEndRefreshToken(user, jwt, isMoblieDevice(userAgent));

                return ResponseEntity.ok(ApiResponse.<LoginResponse>builder()

                                .message(translate(MessageKeys.LOGIN_SUCCESS))

                                .payload(LoginResponse.builder()
                                                .accessToken(token.getToken())
                                                .refreshToken(token.getRefreshToken())
                                                .user(UserResponse.fromUser(user))
                                                .build())
                                .build());
        }

        // @PostMapping("/verify-email")
        // public void confirmRegistration(@RequestBody RegistrationRequest
        // registration) {
        // userService.confirmRegistration(registration);
        // // dinh huong response ve trang login

        // }

        @PostMapping("/send-verification-code")
        public ResponseEntity<ApiResponse<?>> sendVerificationCode(@ModelAttribute VerificationRequest request) {

                return ResponseEntity.ok(ApiResponse.<VerificationResponse>builder()
                                .message(translate(MessageKeys.SEND_CODE_SUCCESS))

                                .payload(userService.sendVerificationCode(request.getEmail())).build());
        }

        // kiểm tra xem thiết bị đang đăng nhập có phải mobile không
        private boolean isMoblieDevice(String userAgent) {
                return userAgent.toLowerCase().contains("mobile");
        }

        @PostMapping("/refresh-token")
        public ResponseEntity<ApiResponse<?>> refreshToken(@ModelAttribute RefreshTokenRequest refreshRequest,
                        HttpServletRequest request) {

                // check is mobile or web login
                String userAgent = request.getHeader("User-Agent");

                Token newToken = userService.refreshToken(refreshRequest.getRefreshToken(), isMoblieDevice(userAgent));

                return ResponseEntity.ok(ApiResponse.<RefreshTokenResponse>builder()

                                .message(translate(MessageKeys.REFRESH_TOKEN_SUCCESS))
                                .payload(RefreshTokenResponse.builder()
                                                .accessToken(newToken.getToken())
                                                .refreshToken(newToken.getRefreshToken())
                                                .build())
                                .build());

        }

        @PostMapping("/logout")
        public void logout(@RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                userService.logout(jwt);

        }

        // controler cho viec edit personal
        @PostMapping("/change-password")
        public ResponseEntity<ApiResponse<?>> changePassword(@ModelAttribute PasswordChangeRequest request,
                        @RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);
                userService.changePassword(request.getOldPassword(),
                                request.getNewPassword(), user);
                return ResponseEntity.ok(ApiResponse.builder()

                                .message(translate(MessageKeys.CHANGE_PASS_SUCCESS))
                                .build());

        }

        @PutMapping()
        public ResponseEntity<ApiResponse<?>> editUser(@ModelAttribute EditUserRequest request,
                        @RequestHeader("Authorization") String token) {

                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<String>builder()
                                .message(translate(MessageKeys.EDIT_PROFILE_SUCCESS))
                                .payload(userService.updateUser(request.getUserName(), request.getImage(), user))
                                .build());
        }

        @GetMapping()
        public ResponseEntity<ApiResponse<?>> getUserDetail(@RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);

                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                                .message(translate(MessageKeys.GET_INFO_SUCCESS))
                                .payload(UserResponse.fromUser(user))
                                .build());
        }

        @DeleteMapping()
        public ResponseEntity<ApiResponse<?>> deleteUser(@RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                userService.deleteUser(jwt);
                return ResponseEntity
                                .ok(ApiResponse.builder().message(translate(MessageKeys.DELETE_USER_SUCCESS)).build());
        }

        // controlerr cho tao va quan ly group family
        @PostMapping("/group")
        public ResponseEntity<ApiResponse<?>> createGroup(@RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);
                Long adminId = userService.createGroup(user);
                return ResponseEntity.ok(ApiResponse.<CreateGroupResponse>builder()
                                .message(translate(MessageKeys.CREATE_GROUP_SUCCESS))
                                .payload(CreateGroupResponse.builder().adminId(adminId).build())
                                .build());
        }

        @PostMapping("/group/add")
        public ResponseEntity<ApiResponse<?>> addMember(@RequestHeader("Authorization") String token,
                        @ModelAttribute GroupRequest request) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                userService.addMember(user, request.getUsername());

                return ResponseEntity
                                .ok(ApiResponse.builder().message(translate(MessageKeys.SUCCESS)).build());
        }

        @DeleteMapping("/group")
        public ResponseEntity<ApiResponse<?>> deleteMember(@RequestHeader("Authorization") String token,
                        @RequestParam("userName") String username) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                userService.deleteMember(user, username);

                return ResponseEntity.ok(
                                ApiResponse.builder().message(translate(MessageKeys.DELETE_MEMBER_SUCCESS)).build());
        }

        @GetMapping("/group")
        public ResponseEntity<ApiResponse<?>> getInfoFamily(@RequestHeader("Authorization") String token) {
                String jwt = token.substring(7);
                User user = userService.getUserDetailsFromToken(jwt);

                return ResponseEntity.ok(ApiResponse.<FamilyInfoResponse>builder()
                                .message(translate(MessageKeys.GET_INFO_GROUP_MEMBER_SUCCESS))
                                .payload(userService.getInfoFamily(user))
                                .build());
        }
}
