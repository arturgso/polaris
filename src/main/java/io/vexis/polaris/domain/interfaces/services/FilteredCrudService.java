package io.vexis.polaris.domain.interfaces.services;

import java.util.List;

public interface FilteredCrudService<CreateDTO, UpdateDTO, ResponseDTO, Entity, ID, FilterDTO>
    extends CrudService<CreateDTO, UpdateDTO, ResponseDTO, Entity, ID> {
  List<ResponseDTO> list(FilterDTO filters);
}
