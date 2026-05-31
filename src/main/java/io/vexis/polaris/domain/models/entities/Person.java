package io.vexis.polaris.domain.models.entities;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tab_persons")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private Short birthdayMonth;
  private Short birthdayDay;

  @CreationTimestamp private Instant createdAt;

  @UpdateTimestamp private Instant updatedAt;
}
