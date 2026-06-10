package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.users.NewUserDTO;
import io.vexis.polaris.domain.models.dtos.users.UserDTO;
import io.vexis.polaris.domain.models.entities.User;
import java.util.UUID;

public interface UserService {
  UserDTO create(NewUserDTO dto);

  User getEntity(UUID id);
}
