package am.egs.socialSite.payload;

import am.egs.socialSite.validation.ValidEmail;
import am.egs.socialSite.validation.ValidPassword;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.util.Objects;

public class UserDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 12, message = "{Size.name}")
    private String name;

    @NotEmpty(message = "{NotEmpty.surName}")
    private String surName;

    @NotNull(message = "{age.notNull}")
    @Min(value = 18, message = "{age.size}")
    private Integer age;

    @Email
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;


    public UserDto() {
    }

    public UserDto(Long id, String email, String password, String name, String surName) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.surName = surName;
    }

    public Long getId() {
        return id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    @Override
    public int hashCode() {
        return Objects.hash(email, password, name, surName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) &&
                Objects.equals(name, userDto.name) &&
                Objects.equals(surName, userDto.surName) &&
                Objects.equals(age, userDto.age) &&
                Objects.equals(email, userDto.email) &&
                Objects.equals(password, userDto.password);
    }
}

