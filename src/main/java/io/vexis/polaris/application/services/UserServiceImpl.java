package io.vexis.polaris.application.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.vexis.polaris.domain.interfaces.repositories.UserRepository;
import io.vexis.polaris.domain.interfaces.services.UserService;
import io.vexis.polaris.domain.models.dtos.users.NewUserDTO;
import io.vexis.polaris.domain.models.dtos.users.UserDTO;
import io.vexis.polaris.domain.models.entities.User;
import io.vexis.polaris.shared.TextUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO create(NewUserDTO dto) {
        String normalizedUsername = TextUtils.normalizeText(dto.username());

        if (repository.existsByUsername(normalizedUsername)) {
            throw new DataIntegrityViolationException("Username já em uso");
        }

        var user = User.builder()
        .username(normalizedUsername)
        .password(passwordEncoder.encode(dto.password()))
        .role("ADMIN")
        .build();

        user = repository.saveAndFlush(user);

        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
