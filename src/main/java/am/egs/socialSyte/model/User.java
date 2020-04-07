package am.egs.socialSyte.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20)", nullable = false)
    private Long id;

    @Email
    @Column(unique = true, name = "email", columnDefinition = "VARCHAR(255)", nullable = false)
    @NotEmpty(message = "{NotEmpty.email}}")
    private String email;

    @Column(name = "name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String name;

    @Column(name = "surname", columnDefinition = "VARCHAR(255)", nullable = false)
    private String surName;

    @Column(name = "password", columnDefinition = "VARCHAR(255)", nullable = false)
    private String password;

    @NotNull
    @Column(name = "age", columnDefinition = "INT(11)", nullable = false)
    private int age;

    @Column(name = "isAccountNonLocked")
    private boolean isAccountNonLocked;

    @Column(name = "expireDate")
    private LocalDateTime expireDate;

    @Column(name = "isEnabled")
    private boolean isEnabled;

    @Column(name = "tryNumberToLogin")
    private int tryNumber;

    @Column(name = "lokedTime")
    private LocalDateTime lokedTime;

    @Column(name = "unLokedTime")
    private LocalDateTime unLokedTime;

    @Column(name = "activationCode")
    private String activationCode;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surname) {
        this.surName = surname;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getTryNumber() {
        return tryNumber;
    }

    public void setTryNumber(int tryNumber) {
        this.tryNumber = tryNumber;
    }

    public LocalDateTime getLokedTime() {
        return lokedTime;
    }

    public void setLokedTime(LocalDateTime lokedTime) {
        this.lokedTime = lokedTime;
    }

    public LocalDateTime getUnLokedTime() {
        return unLokedTime;
    }

    public void setUnLokedTime(LocalDateTime unLokedTime) {
        this.unLokedTime = unLokedTime;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", expireDate=" + expireDate +
                ", isEnabled=" + isEnabled +
                ", tryNumber=" + tryNumber +
                ", lokedTime=" + lokedTime +
                ", activationCode='" + activationCode + '\'' +
                ", emailVerified=" + emailVerified +
                ", roles=" + roles +
                '}';
    }
}
