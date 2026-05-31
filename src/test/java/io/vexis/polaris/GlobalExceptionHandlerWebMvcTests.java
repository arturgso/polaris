package io.vexis.polaris;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.vexis.polaris.application.controllers.GiftsController;
import io.vexis.polaris.application.controllers.PersonsController;
import io.vexis.polaris.domain.exceptions.PersonNotFoundException;
import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.interfaces.services.PersonsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {PersonsController.class, GiftsController.class})
class GlobalExceptionHandlerWebMvcTests {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private PersonsService personsService;

  @MockitoBean private GiftsService giftsService;

  @Test
  void shouldReturnFriendlyErrorWhenResourceDoesNotExist() throws Exception {
    var personId = 999L;
    doThrow(new PersonNotFoundException()).when(personsService).delete(personId);

    mockMvc
        .perform(delete("/persons/{id}", personId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Recurso não encontrado."))
        .andExpect(jsonPath("$.path").value("/persons/" + personId))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void shouldReturnFriendlyErrorWhenRequiredFieldIsBlank() throws Exception {
    mockMvc
        .perform(
            post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"name":""}
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Verifique os dados enviados."))
        .andExpect(jsonPath("$.path").value("/persons"))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void shouldReturnFriendlyErrorWhenGiftIdIsInvalid() throws Exception {
    mockMvc
        .perform(get("/gifts/{personId}", "not-a-number"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Algum dado enviado é inválido."))
        .andExpect(jsonPath("$.path").value("/gifts/not-a-number"))
        .andExpect(jsonPath("$.timestamp").exists());
  }
}
