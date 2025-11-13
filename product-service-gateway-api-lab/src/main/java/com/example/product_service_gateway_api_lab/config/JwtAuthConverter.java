package com.example.product_service_gateway_api_lab.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtAuthConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        Collection<SimpleGrantedAuthority> authorities = extractAuthorities(jwt);

        String principal = jwt.getClaimAsString("preferred_username");
        if (principal == null) {
            principal = jwt.getSubject(); // fallback if preferred_username not present
        }

        return Mono.just(new JwtAuthenticationToken(jwt, authorities, principal));
    }

    private Collection<SimpleGrantedAuthority> extractAuthorities(Jwt jwt) {
        // Roles from realm_access.roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null || realmAccess.isEmpty()) {
            return List.of();
        }

        Object rolesObj = realmAccess.get("roles");
        if (!(rolesObj instanceof List<?> roles)) {
            return List.of();
        }
        

        var realmAuthorities = roles.stream()
                .filter(role -> role instanceof String)
                .map(role -> "ROLE_" + role.toString().toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        

        System.out.println("Roles found in the token : " + roles);
	    System.out.println("Authorities in Spring : " + realmAuthorities);
	    
        return realmAuthorities;
    }
}


/*
implements Converter<Jwt, AbstractAuthenticationToken> {
	private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		Collection<GrantedAuthority> authorities = getAuthorities(jwt);
		return new JwtAuthenticationToken(jwt, authorities);
	}

	
	
	private Collection<GrantedAuthority> getAuthorities(Jwt jwt) {
	    // commence avec les autorités par défaut
	    Collection<GrantedAuthority> grantedAuthorities = defaultGrantedAuthoritiesConverter.convert(jwt);

	    // récupération correcte des rôles Keycloak dans realm_access
	    Object realmAccess = jwt.getClaims().get("realm_access");
	    Collection<String> roles = Collections.emptyList();

	    if (realmAccess instanceof Map) {
	        Object roleObj = ((Map<?, ?>) realmAccess).get("roles");
	        if (roleObj instanceof Collection) {
	            roles = (Collection<String>) roleObj;
	        }
	    }

	    // transformation des rôles en autorités Spring
	    Collection<GrantedAuthority> realmAuthorities = roles.stream()
	            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
	            .collect(Collectors.toList());

	    System.out.println("Roles found in the token : " + roles);
	    System.out.println("Authorities in Spring : " + realmAuthorities);

	    grantedAuthorities.addAll(realmAuthorities);
	    return grantedAuthorities;
	}
}
*/
