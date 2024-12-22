package com.hust.smart_Shopping.services.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hust.smart_Shopping.components.JwtTokenUtil;
import com.hust.smart_Shopping.constants.AppConstants;
import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
import com.hust.smart_Shopping.exceptions.payload.ExpiredTokenException;
import com.hust.smart_Shopping.models.Token;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.repositories.TokenRepository;
import com.hust.smart_Shopping.repositories.UserRepository;
import com.hust.smart_Shopping.services.TokenService;
import com.hust.smart_Shopping.utils.MessageKeys;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    @Transactional
    @Override
    public Token addTokenEndRefreshToken(User user, String token, boolean isMobile) {
        List<Token> userTokens = tokenRepository.findByUser(user);

        int tokensCount = userTokens.size();
        // số lượng token vượt quá giới hạn
        if (tokensCount >= AppConstants.MAX_TOKEN_PER_USER) {
            boolean hasNoMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNoMobileToken) {
                tokenToDelete = userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(userTokens.get(0));
            } else {
                // chúng ta sẽ xoá token đầu tiên trong danh sách
                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .refreshToken(jwtTokenUtil.generateRefreshToken(user))
                .tokenType("Bearer")
                .expirationTime(Instant.now().plusMillis(expiration))
                .refreshExpirationTime(Instant.now().plusMillis(expirationRefreshToken))
                .revoked(false)
                .expired(false)
                .isMobile(isMobile)
                .build();

        return tokenRepository.save(newToken);
    }

    public Token verifyRefreshToken(String refreshToken) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.ERROR_REFRESH_TOKEN));

        if (token.getRefreshExpirationTime().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(token);
            throw new ExpiredTokenException(MessageKeys.ERROR_REFRESH_TOKEN);
        }

        return token;
    }

    public void deleteTokenWithToken(Token token) {
        tokenRepository.delete(token);
    }

    public void deleteTokenWithJwt(String jwt) {
        Token token = tokenRepository.findByToken(jwt)
                .orElseThrow(() -> new DataNotFoundException(""));

        log.debug("delete token: {}", token);
        tokenRepository.delete(token);
    }

}
