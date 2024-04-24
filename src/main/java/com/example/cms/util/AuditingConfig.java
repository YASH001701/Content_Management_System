package com.example.cms.util;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

	@Bean
	AuditorAware<String> auditor() {
		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();

		return () -> Optional.of(

				SecurityContextHolder.getContext().getAuthentication().getName());

//		 
	}

}
