package am.egs.socialSite.security;

import am.egs.socialSite.model.Role;
import am.egs.socialSite.model.User;
import am.egs.socialSite.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private Long id;

    private String email;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private LocalDateTime expireDate;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isEnabled;

    private UserService userService;

    public UserPrincipal(Long id, String email, String password, boolean isAccountNonLocked,
                         boolean isEnabled, LocalDateTime expireDate, boolean isAccountNonExpired, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isEnabled = isEnabled;
        this.expireDate = expireDate;
    }

    public UserPrincipal() {

    }

    static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Role::getRole)
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.isAccountNonLocked(),
                user.isEnabled(),
                user.getExpireDate(),
                user.isAccountNonExpired(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (expireDate.isBefore(currentDateTime)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }
}