package io.vexis.polaris.domain.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tab_gifts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Gift {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String link;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id")
  private Person giftFor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id")
  private Event event;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "status_id")
  private GiftStatus status;

  @CreationTimestamp private Instant createdAt;

  @UpdateTimestamp private Instant updatedAt;
}
