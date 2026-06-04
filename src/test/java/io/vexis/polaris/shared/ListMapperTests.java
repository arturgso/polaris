package io.vexis.polaris.shared;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class ListMapperTests {

  @Test
  void shouldMapListItemsInOrder() {
    var result = ListMapper.createResponseList(List.of("alice", "bob", "carol"), String::length);

    assertIterableEquals(List.of(5, 3, 5), result);
  }

  @Test
  void shouldReturnEmptyListWhenSourceListIsEmpty() {
    var result = ListMapper.createResponseList(List.<String>of(), String::toUpperCase);

    assertTrue(result.isEmpty());
  }
}
