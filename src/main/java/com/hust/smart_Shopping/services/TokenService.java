package com.hust.smart_Shopping.services;

import com.hust.smart_Shopping.models.Token;
import com.hust.smart_Shopping.models.User;

public interface TokenService {
    Token addTokenEndRefreshToken(User user, String token, boolean isMobile);

    public Token verifyRefreshToken(String refreshToken);

    public void deleteTokenWithToken(Token token);

    public void deleteTokenWithJwt(String jwt);

}
