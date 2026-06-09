package io.vexis.polaris.shared.utils;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class EntityUtils {

  public static <T, ID> T findOrThrow(JpaRepository<T, ID> repository, ID id) {
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
  }
}
