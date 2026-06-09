package io.vexis.polaris.shared.abstracts;

import java.time.Instant;

import lombok.Builder;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuperBuilder
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class ListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String title;

    @Builder.Default
    private Boolean inVault = false;

    @CreationTimestamp
    @Column(updatable=false, nullable=false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}