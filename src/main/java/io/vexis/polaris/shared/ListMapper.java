package io.vexis.polaris.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListMapper {
  public static <E, T> List<T> createResponseList(List<E> entities, Function<E, T> mapper) {
    List<T> responseList = new ArrayList<>();

    for (E entity : entities) {
      responseList.add(mapper.apply(entity));
    }

    return responseList;
  }
}
