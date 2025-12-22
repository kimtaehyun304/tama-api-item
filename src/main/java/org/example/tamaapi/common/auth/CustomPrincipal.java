package org.example.tamaapi.common.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Collection;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal {
    private String bearerJwt;
    private  Long memberId;
}