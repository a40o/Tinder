package com.volasoftware.tinder.services;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditableService implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable("Kindston").filter(s -> !s.isEmpty());
    }
}
