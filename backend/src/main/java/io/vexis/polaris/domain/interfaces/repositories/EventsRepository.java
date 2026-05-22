package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventsRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByName(String name);
}
