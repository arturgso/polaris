package io.vexis.polaris.domain.models.entities;

import io.vexis.polaris.domain.enums.Events;
import io.vexis.polaris.domain.enums.GiftStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tab_gifts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person giftFor;

    @Enumerated(EnumType.STRING)
    private Events event;

    @Enumerated(EnumType.STRING)
    private GiftStatus status;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
