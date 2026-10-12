package com.example.aviator.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

//public class User {
//    package com.example.aviator.user;


@Entity
    @Table(name = "users")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class User {



    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true, length = 50)
        private String username;

        @Column(nullable = false, unique = true, length = 100)
        private String email;

        @Column(nullable = false)
        private String password;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        @Builder.Default
        private Role role = Role.USER;

        @Column(nullable = false)
        @Builder.Default
        private boolean enabled = true;

        @Column(nullable = false, updatable = false)
        @Builder.Default
        private LocalDateTime createdAt = LocalDateTime.now();

        private LocalDateTime updatedAt;

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }

    public Long getId() {
        return id;
    }
    }

