package com.example.product_service_gateway_api_lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

/*
@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
				.oauth2ResourceServer(oauth2 -> oauth2.jwt());
		return http.build();
	}
}
*/

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	
	// Add this field
	private final JwtAuthConverter jwtAuthConverter;
	// Constructor injection
	public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
	this.jwtAuthConverter = jwtAuthConverter;
	}

	

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/actuator/**").permitAll()  // allow actuator
                .pathMatchers("/api/public/**").permitAll() // example public endpoints
                .pathMatchers("/api/products").hasRole("ADMIN")
                .anyExchange().authenticated()
            )
            /*
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt()
            );
        */
        .oauth2ResourceServer(oauth2 -> oauth2
        		.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
        		);

        return http.build();
    }
}