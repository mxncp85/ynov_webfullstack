package com.ynov.adventures.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")
        }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Nom d'affichage choisi à l'inscription. */
    @Column(nullable = false, length = 50)
    private String username;

    /** Email — utilisé comme identifiant de connexion (principal Spring Security). */
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /**
     * Retourne l'email comme principal Spring Security.
     * L'email est utilisé pour l'authentification (login) et comme subject du JWT.
     * Lombok ne génère PAS de getUsername() depuis le champ 'username' car cette méthode existe déjà.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Retourne le nom d'affichage (champ 'username' en base).
     * Nécessaire puisque getUsername() est réservé par UserDetails pour retourner l'email.
     */
    public String getDisplayUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
