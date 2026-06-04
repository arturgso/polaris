package io.vexis.polaris.domain.interfaces.services;

import java.util.List;

public interface ListCrudService<CreateDTO, UpdateDTO, ResponseDTO, Entity, ID>
    extends CrudService<CreateDTO, UpdateDTO, ResponseDTO, Entity, ID> {
  List<ResponseDTO> list();
}
