package com.example.final_case_social_web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "userTable")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false, unique = true)
    private String username;

    private String password;

    private String confirmPassword;

    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;

    private String fullName;
    private String email;
    @Column(length = 50)
    private String phone;
    @Column(length = 500)
    private String favorite;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Phải là ngày trong quá khứ")
    private LocalDate dateOfBirth;
    @Column(length = 1000)
    private String avatar = "assets/images/defaultAva.png";

    @Column(length = 1000)
    private String cover = "assets/images/face-map_ccexpress.png";

    private String address;

    private String status;

    private String gender;

    private String education;
    private Date createAt = new Date();
}
