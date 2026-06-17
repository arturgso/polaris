package io.vexis.polaris.domain.interfaces.services;

public interface CrudService<CreateDTO, UpdateDTO, ResponseDTO, Entity, ID> {
  ResponseDTO create(CreateDTO dto);

  Entity getEntity(ID id);

  void update(UpdateDTO dto, ID id);

  default void update(UpdateDTO dto, ID id, String vaultPassword) {
    update(dto, id);
  }

  void delete(ID id);

  default void delete(ID id, String vaultPassword) {
    delete(id);
  }
}
