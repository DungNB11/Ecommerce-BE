package com.dungnb.be.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String fullName;
    private String phone;

    @Column(nullable = false)
    private String addressLine1;
    private String addressLine2;

    @Column(nullable = false)
    private String city;
    private String district;
    private String ward;
    private String postalCode;

    @Builder.Default
    private String country = "Vietnam";

    @Builder.Default
    private Boolean isDefault = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AddressType addressType = AddressType.HOME;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum AddressType {
        HOME, OFFICE, OTHER
    }
}
