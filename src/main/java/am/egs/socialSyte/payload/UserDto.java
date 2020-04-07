package am.egs.socialSyte.payload;

import am.egs.socialSyte.validator.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;


public class UserDto {
    private Long id;

    @Email(message = "{Email.email}")
    @UniqueEmail(message = "{Unique.email}")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "{Pattern.password}}")
    private String password;

    @Size(min = 3, max = 50, message = "{Size.name}}")
    private String name;

    @NotEmpty(message = "{NotEmpty.name}")
    private String surName;

    private Date createdDate;

    public UserDto() {
    }

    public UserDto(Long id, String email, String password, String name, String surName, Date createdDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surName = surName;
        this.createdDate = createdDate;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id.equals(userDto.id) &&
                email.equals(userDto.email) &&
                password.equals(userDto.password) &&
                name.equals(userDto.name) &&
                surName.equals(userDto.surName) &&
                createdDate.equals(userDto.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, surName, createdDate);
    }
}

