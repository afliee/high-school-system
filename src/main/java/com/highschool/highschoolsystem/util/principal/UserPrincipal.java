package com.highschool.highschoolsystem.util.principal;

import com.highschool.highschoolsystem.config.Role;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPrincipal implements UserDetails {
    private String id;
    private String username;
    private String password;
    private Role role;

    public UserPrincipal(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public UserPrincipal(TeacherEntity teacher) {
        this.id = teacher.getId();
        this.username = teacher.getName();
        this.password = teacher.getPassword();
        this.role = teacher.getRole();
    }

    public UserPrincipal(StudentEntity student) {
        this.id = student.getId();
        this.username = student.getName();
        this.password = student.getPassword();
        this.role = student.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
