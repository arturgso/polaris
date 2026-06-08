package io.vexis.polaris.domain.interfaces.services;

public interface CrudService<CreateDTO, UpdateDTO, ResponseDTO, Entity, ID> {
  ResponseDTO create(CreateDTO dto);

  Entity getEntity(ID id);

  void update(UpdateDTO dto, ID id);

  void delete(ID id);
}
