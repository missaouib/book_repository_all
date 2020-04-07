package am.egs.socialSite.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT(20)", nullable = false)
    private Long id;


    @Column(unique = true, name = "email", columnDefinition = "VARCHAR(255)", nullable = false)
    private String email;

    @Column(name = "name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String name;

    @Column(name = "surname", columnDefinition = "VARCHAR(255)", nullable = false)
    private String surName;

    @Column(name = "password", columnDefinition = "VARCHAR(255)", nullable = false)
    private String password;

    @Column(name = "age", columnDefinition = "INT(11)", nullable = false)
    private Integer age;

    @Column(name = "tryNumberToLogin")
    private int tryNumber;

    @Column(name = "expireDate")
    private LocalDateTime expireDate;

    @Column(name = "lockedTime")
    private LocalDateTime lockedTime;

    @Column(name = "unLockedTime")
    private LocalDateTime unLockedTime;

    @Column(name = "activationCode")
    private String activationCode;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Column(name = "isEnabled")
    private boolean isEnabled;

    @Column(name = "isAccountNonExpired")
    private boolean isAccountNonExpired;

    @Column(name = "isAccountNonLocked")
    private boolean isAccountNonLocked;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getTryNumber() {
        return tryNumber;
    }

    public void setTryNumber(int tryNumber) {
        this.tryNumber = tryNumber;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public LocalDateTime getLockedTime() {
        return lockedTime;
    }

    public void setLockedTime(LocalDateTime lokedTime) {
        this.lockedTime = lokedTime;
    }

    public LocalDateTime getUnLockedTime() {
        return unLockedTime;
    }

    public void setUnLockedTime(LocalDateTime unLokedTime) {
        this.unLockedTime = unLokedTime;
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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
                ", tryNumber=" + tryNumber +
                ", expireDate=" + expireDate +
                ", lockedTime=" + lockedTime +
                ", unLockedTime=" + unLockedTime +
                ", activationCode='" + activationCode + '\'' +
                ", emailVerified=" + emailVerified +
                ", isEnabled=" + isEnabled +
                ", isAccountNonExpired=" + isAccountNonExpired +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", roles=" + roles +
                '}';
    }
}
