package org.example.groworders.domain.users.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.groworders.domain.users.model.dto.EmailVerify;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountId;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private LocalDate birthDate;
    @Setter
    private String profileImage;
    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    List<EmailVerify> emailVerifyList;

    public void userVerify() {
        this.enabled = true;
    }

}