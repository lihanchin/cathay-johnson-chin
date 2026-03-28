package org.example.cathayjohnsonchin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @CurrentTimestamp
    private LocalDateTime createdAt;

    private String createdUser = "system";

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String updatedUser = "system";

    public void setCode(String code) {
        this.code = code.trim().toLowerCase();
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName.trim();
    }
}
