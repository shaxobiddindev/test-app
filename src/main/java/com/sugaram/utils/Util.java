package com.sugaram.utils;

import com.sugaram.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public interface Util {
    String[] openUrl = {
            "/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
