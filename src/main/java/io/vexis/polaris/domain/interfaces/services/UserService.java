package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.users.NewUserDTO;
import io.vexis.polaris.domain.models.dtos.users.UserDTO;

public interface UserService {
    UserDTO create(NewUserDTO dto);
}
