package com.hust.smart_Shopping.services;

import org.springframework.web.multipart.MultipartFile;

import com.hust.smart_Shopping.dtos.user.FamilyInfoResponse;
import com.hust.smart_Shopping.dtos.user.RegistrationRequest;
import com.hust.smart_Shopping.dtos.user.RegistrationResponse;
import com.hust.smart_Shopping.dtos.user.UserRequest;
import com.hust.smart_Shopping.dtos.user.VerificationResponse;
import com.hust.smart_Shopping.models.Token;
import com.hust.smart_Shopping.models.User;

public interface UserService {
    RegistrationResponse createUser(UserRequest userRequest);

    void confirmVerification(RegistrationRequest registration);

    String login(String email, String password);

    User getUserDetailsFromToken(String token);

    VerificationResponse sendVerificationCode(String email);

    Token refreshToken(String refreshToken, Boolean isMobile);

    void logout(String jwt);

    void changePassword(String currentPassword, String newPassword, User user);

    String updateUser(String nickName, MultipartFile image, User user);

    void deleteUser(String jwt);

    Long createGroup(User user);

    void addMember(User leader, String username);

    void deleteMember(User leader, String username);

    FamilyInfoResponse getInfoFamily(User user);

}
