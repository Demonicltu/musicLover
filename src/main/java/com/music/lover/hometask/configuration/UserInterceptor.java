package com.music.lover.hometask.configuration;

import com.music.lover.hometask.constant.ApiHttpHeaders;
import com.music.lover.hometask.dto.AuthenticationInformation;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.RequestException;
import com.music.lover.hometask.exception.UserNotFoundException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.service.UserService;
import org.hibernate.internal.util.StringHelper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserInterceptor.class);

    private final UserService userService;

    public UserInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        logger.info("[B] User check");

        String userKeyHeader = request.getHeader(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue());
        if (StringHelper.isEmpty(userKeyHeader)) {
            throw new RequestException(ApplicationError.MISSING_USER_KEY);
        }

        User user;
        try {
            user = userService.getUserByUUID(userKeyHeader);
        } catch (UserNotFoundException e) {
            throw new RequestException(ApplicationError.USER_NOT_FOUND);
        }

        AuthenticationInformation authenticationInformation = new AuthenticationInformation(user);
        request.setAttribute("authentication", authenticationInformation);

        logger.info("[E] user check");

        return true;
    }

}
