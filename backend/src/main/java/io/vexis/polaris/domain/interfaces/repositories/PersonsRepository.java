package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonsRepository extends JpaRepository<Person, UUID> {
}
