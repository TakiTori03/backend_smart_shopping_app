package com.hust.smart_Shopping.services.impl;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.smart_Shopping.components.JwtTokenUtil;

import com.hust.smart_Shopping.constants.AppConstants;
import com.hust.smart_Shopping.constants.AppConstants.RoleType;
import com.hust.smart_Shopping.constants.Enum.VerificationType;
import com.hust.smart_Shopping.dtos.user.FamilyInfoResponse;
import com.hust.smart_Shopping.dtos.user.RegistrationRequest;
import com.hust.smart_Shopping.dtos.user.RegistrationResponse;
import com.hust.smart_Shopping.dtos.user.UserRequest;
import com.hust.smart_Shopping.dtos.user.UserResponse;
import com.hust.smart_Shopping.dtos.user.VerificationResponse;
import com.hust.smart_Shopping.exceptions.AppException;
import com.hust.smart_Shopping.exceptions.ErrorCode;
import com.hust.smart_Shopping.exceptions.payload.BusinessLogicException;
import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
import com.hust.smart_Shopping.exceptions.payload.ExpiredTokenException;
import com.hust.smart_Shopping.exceptions.payload.VerificationException;
import com.hust.smart_Shopping.models.Family;
import com.hust.smart_Shopping.models.Role;
import com.hust.smart_Shopping.models.Token;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.models.UserFamily;
import com.hust.smart_Shopping.models.Verification;
import com.hust.smart_Shopping.repositories.FamilyReposiroty;
import com.hust.smart_Shopping.repositories.RoleRepository;
import com.hust.smart_Shopping.repositories.UserFamilyRepository;
import com.hust.smart_Shopping.repositories.UserRepository;
import com.hust.smart_Shopping.repositories.VerificationRepository;
import com.hust.smart_Shopping.services.MailService;
import com.hust.smart_Shopping.services.TokenService;
import com.hust.smart_Shopping.services.UserService;
import com.hust.smart_Shopping.utils.MessageKeys;
import com.hust.smart_Shopping.utils.RadomUntil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final VerificationRepository verificationRepository;
        private final PasswordEncoder passwordEncoder;
        private final MailService mailService;
        private final TokenService tokenService;
        private final JwtTokenUtil jwtTokenUtil;
        private final AuthenticationManager authenticationManager;
        private final UserFamilyRepository userFamilyRepository;
        private final FamilyReposiroty familyReposiroty;

        @Override
        @Transactional
        public RegistrationResponse createUser(UserRequest userRequest) {
                // register new user
                String email = userRequest.getEmail();
                // kiểm tra xem email đã tồn tại hay chưa
                if (userRepository.existsByEmail(email)) {
                        throw new DataIntegrityViolationException(
                                        MessageKeys.EMAIL_EXISTED);
                }
                Role role = roleRepository.findByName(RoleType.USER)
                                .orElseThrow(() -> new DataNotFoundException(MessageKeys.ROLE_NOT_FOUND));

                // convert userRequest -> user
                User newUser = new User();
                newUser.setEmail(userRequest.getEmail());
                newUser.setName(userRequest.getName());
                String nickname;
                // create nickname unique
                {
                        String baseUsername = userRequest.getName().toLowerCase().replaceAll("\\s+", "");
                        Random random = new Random();
                        // Generate random number and append to base nickname
                        do {
                                int randomNumber = random.nextInt(10000); // Random number between 0 and 9999
                                nickname = baseUsername + randomNumber;
                        } while (userRepository.existsByNickname(nickname));

                }
                // set user name
                newUser.setNickname(nickname);

                // set password
                String password = userRequest.getPassword();
                String encodedPassword = passwordEncoder.encode(password);
                newUser.setPassword(encodedPassword);

                newUser.setRole(role);
                newUser.setActivated(true); // mở tài khoản
                newUser.setVerified(false); // chua activated email

                // mac dinh region new user
                newUser.setCountryCode(AppConstants.DEFAULT_COUNTRYCODE);
                newUser.setTimezone(AppConstants.DEFAULT_TIMEZONE);
                newUser.setLanguage(AppConstants.DEFAULT_LANGUAGE);

                User user = userRepository.save(newUser);

                // Create new verification entity and set user, token
                Verification verification = new Verification();
                String token = jwtTokenUtil.generateToken(user);
                String code = RadomUntil.generateConfirmCode();

                verification.setUser(user);
                verification.setCode(code);
                verification.setToken(token);
                verification.setExpiredAt(Instant.now().plus(10, ChronoUnit.MINUTES));
                verification.setType(VerificationType.VERIFICATION);

                verificationRepository.save(verification);

                // Send email
                // Map<String, Object> attributes = Map.of(
                // "token", token,
                // "link", MessageFormat.format("{0}/signup?userId={1}",
                // AppConstants.FRONTEND_HOST, user.getId()));
                // mailService.sendVerificationToken(user.getEmail(), attributes);
                return RegistrationResponse.builder()
                                .user(UserResponse.fromUser(user))
                                .confirmToken(token)
                                .build();
        }

        @Override
        public void confirmVerification(RegistrationRequest registration) {
                Verification verification = verificationRepository.findByCode(registration.getCode())
                                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));

                boolean validVerification = verification.getToken().equals(registration.getToken())
                                && verification.getExpiredAt().isAfter(Instant.now())
                                && verification.getType().equals(VerificationType.VERIFICATION);

                if (validVerification) {
                        // (1) Set status code and delete row verification
                        User user = verification.getUser();
                        user.setVerified(true); // Verified

                        userRepository.save(user);
                        verificationRepository.delete(verification);
                }

                boolean tokenIsExpired = verification.getToken().equals(registration.getToken())
                                && !verification.getExpiredAt().isAfter(Instant.now())
                                && verification.getType().equals(VerificationType.VERIFICATION);

                if (tokenIsExpired) {
                        // (1) Set status code and delete row verification
                        User user = verification.getUser();
                        String token = jwtTokenUtil.generateToken(user);

                        // tao token moi cho verification
                        verification.setToken(token);
                        verification.setExpiredAt(Instant.now().plus(10, ChronoUnit.MINUTES));
                        verification.setType(VerificationType.VERIFICATION);

                        verificationRepository.save(verification);

                        // Map<String, Object> attributes = Map.of(
                        // "token", token,
                        // "link",
                        // MessageFormat.format("{0}/signup?userId={1}", AppConstants.FRONTEND_HOST,
                        // user.getId()));
                        // mailService.sendVerificationToken(verification.getUser().getEmail(),
                        // attributes);

                        throw new ExpiredTokenException("Token is expired, please check your email to get new token!");
                }

                if (!verification.getToken().equals(registration.getToken())) {
                        throw new VerificationException("Invalid token");
                }

        }

        @Override
        public String login(String email, String password) {
                try {
                        // Tìm user theo email
                        User user = userRepository.findByEmail(email)
                                        .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));

                        // Kiểm tra xem user đã được active hay chưa
                        if (!user.isActivated()) {
                                throw new DataNotFoundException(MessageKeys.USER_ID_LOCKED);
                        }

                        // Xác thực với Spring Security
                        authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(email, password,
                                                        user.getAuthorities()));

                        // Tạo JWT token
                        return jwtTokenUtil.generateToken(user);

                } catch (DataNotFoundException ex) {
                        throw ex; // Ném lại exception để được xử lý ở tầng global
                } catch (BadCredentialsException ex) {
                        throw new DataNotFoundException("Invalid credentials"); // Xử lý lỗi xác thực sai
                } catch (Exception ex) {
                        throw new RuntimeException("Unexpected error occurred while logging in", ex); // Lỗi không xác
                                                                                                      // định
                }
        }

        @Override
        public User getUserDetailsFromToken(String token) {
                if (jwtTokenUtil.isTokenExpirated(token)) {
                        throw new ExpiredTokenException(MessageKeys.TOKEN_EXPIRATION_TIME);
                }

                String email = jwtTokenUtil.extractEmail(token);
                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));

        }

        @Override
        public VerificationResponse sendVerificationCode(String email) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));

                Verification verification = verificationRepository.findByUser(user)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_VERIFIED));

                if (verification.getType() != VerificationType.VERIFICATION)
                        throw new AppException(ErrorCode.USER_VERIFIED);

                // tao confirm token moi va gui ve cho user
                String token = jwtTokenUtil.generateToken(user);
                verification.setToken(token);
                verification.setExpiredAt(Instant.now().plus(5, ChronoUnit.MINUTES));

                verificationRepository.save(verification);

                Map<String, Object> attributes = Map.of(
                                "token", token,
                                "code", verification.getCode(),
                                "link", MessageFormat.format("{0}/signup?userId={1}", AppConstants.FRONTEND_HOST,
                                                user.getId()));
                // mailService.sendVerificationToken(email, attributes);

                return VerificationResponse.builder().confirmToken(token).build();
        }

        @Override
        public Token refreshToken(String refreshToken, Boolean isMobile) {
                // xac thuc refresh token dung 0
                Token token = tokenService.verifyRefreshToken(refreshToken);

                // kiem tra token da revoked hay expired hay chua
                if (token.isExpired() || token.isRevoked()) {
                        throw new ExpiredTokenException("");
                }
                // lay user
                User user = token.getUser();

                // xoa token cu
                tokenService.deleteTokenWithToken(token);

                // tao token moi cung voi refresh token moi
                String jwt = jwtTokenUtil.generateToken(user);

                return tokenService.addTokenEndRefreshToken(user, jwt, isMobile);
        }

        @Override
        public void logout(String jwt) {
                tokenService.deleteTokenWithJwt(jwt);
        }

        @Override
        public void changePassword(String currentPassword, String newPassword, User user) {

                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentPassword, currentEncryptedPassword)) {
                        throw new AppException(ErrorCode.INVALID_PASSWORD);
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                log.debug("Changed password for User: {}", user);
        }

        @Override
        public void deleteUser(String jwt) {
                User user = getUserDetailsFromToken(jwt);

                userRepository.delete(user);
                log.debug("delete user: {}", user);
        }

        @Override
        public Long createGroup(User user) {
                // kiem tra user da co trong group family nao chua
                if (userFamilyRepository.existsByUser(user))
                        throw new BusinessLogicException("");

                // tao group family + leader
                Family family = new Family();
                family.setLeader(user);
                familyReposiroty.save(family);

                // tao quan he user family
                UserFamily userFamily = new UserFamily();
                userFamily.setFamily(family);
                userFamily.setUser(user);

                // khoi tao role leader cho user
                if (roleRepository.findByName(AppConstants.RoleType.LEADER).isEmpty()) {
                        Role leaderRole = new Role(AppConstants.RoleType.LEADER, "leader pers");

                        roleRepository.save(leaderRole);
                        // tao leader role trong user family
                }
                Role leaderRole = roleRepository.findByName(AppConstants.RoleType.LEADER).get();

                userFamily.setRole(leaderRole);
                userFamilyRepository.save(userFamily);

                return family.getLeader().getId();

        }

        @Override
        public void addMember(User leader, String username) {
                // check nguoi dung da tao group family chua
                UserFamily userFamily = userFamilyRepository.findByUser(leader)
                                .orElseThrow(() -> new BusinessLogicException(""));
                if (!userFamily.getRole().getName().equals(AppConstants.RoleType.LEADER))
                        throw new BusinessLogicException("");

                // check username ton tai hay khong va da thuoc 1 group family nao chua
                User member = userRepository.findByNickname(username)
                                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));

                if (userFamilyRepository.findByUser(member).isPresent())
                        throw new BusinessLogicException("");

                UserFamily newUserFamily = new UserFamily();
                newUserFamily.setFamily(userFamily.getFamily());
                newUserFamily.setUser(member);

                // khoi tao role member cho user
                if (roleRepository.findByName(AppConstants.RoleType.MEMBER).isEmpty()) {
                        Role memberRole = new Role(AppConstants.RoleType.MEMBER, "member pers");

                        roleRepository.save(memberRole);
                        // tao member role trong user family
                }
                Role memberRole = roleRepository.findByName(AppConstants.RoleType.MEMBER).get();

                newUserFamily.setRole(memberRole);
                userFamilyRepository.save(newUserFamily);

        }

        @Override
        public void deleteMember(User leader, String username) {
                // check nguoi dung da tao group family chua
                UserFamily userFamily = userFamilyRepository.findByUser(leader)
                                .orElseThrow(() -> new BusinessLogicException(""));
                if (!userFamily.getRole().getName().equals(AppConstants.RoleType.LEADER))
                        throw new BusinessLogicException("");

                // check username ton tai hay khong va da thuoc 1 group family nao chua
                User member = userRepository.findByNickname(username)
                                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));

                UserFamily deleteUserFamily = userFamilyRepository.findByUser(member)
                                .orElseThrow(() -> new BusinessLogicException(""));

                userFamilyRepository.delete(deleteUserFamily);
                log.debug("delete member: {} from family : {}", member, userFamily.getFamily());
        }

        @Override
        public FamilyInfoResponse getInfoFamily(User user) {
                // check user da co group family chua
                UserFamily userFamily = userFamilyRepository.findByUser(user)
                                .orElseThrow(() -> new BusinessLogicException(""));

                Family family = userFamily.getFamily();

                List<User> members = userFamilyRepository.findByFamily(family).stream().map(uf -> uf.getUser())
                                .collect(Collectors.toList());
                Long groupAdmin = family.getLeader().getId();

                return FamilyInfoResponse
                                .builder().groupAdmin(groupAdmin).members(members.stream()
                                                .map(u -> UserResponse.fromUser(u)).collect(Collectors.toList()))
                                .build();

        }
}
