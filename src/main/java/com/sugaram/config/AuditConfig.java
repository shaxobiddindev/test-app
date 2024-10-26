package com.sugaram.config;


import com.sugaram.entity.User;
import com.sugaram.utils.Util;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditConfig implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            return Optional.of(principal.getId());
        }
        return Optional.empty();
    }
}
