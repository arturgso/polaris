package io.vexis.polaris.domain.interfaces.services;

import java.util.UUID;

import io.vexis.polaris.domain.models.dtos.users.NewUserDTO;
import io.vexis.polaris.domain.models.dtos.users.UserDTO;
import io.vexis.polaris.domain.models.entities.User;

public interface UserService {
  UserDTO create(NewUserDTO dto);
  User getEntity(UUID id);
}
