package org.example.cathayjohnsonchin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@ToString
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String chineseName;

    private LocalDateTime createdAt;

    private String createdUser = "system";

    private LocalDateTime updatedAt;

    private String updatedUser = "system";

    public void setCode(String code) {
        this.code = code.trim().toLowerCase();
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName.trim();
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.createdUser == null) {
            this.createdUser = "system";
        }
        if (this.updatedUser == null) {
            this.updatedUser = "system";
        }

    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.updatedUser == null) {
            this.updatedUser = "system";
        }
    }
}
