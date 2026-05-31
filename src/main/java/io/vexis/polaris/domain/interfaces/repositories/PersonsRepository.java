package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonsRepository extends JpaRepository<Person, Long> {}
