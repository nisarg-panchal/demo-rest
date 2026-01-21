package com.nisarg.spring.demorest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tools.jackson.databind.ObjectMapper;
import com.nisarg.spring.demorest.dto.PersonDto;
import com.nisarg.spring.demorest.service.PersonService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PersonController.class)
@Import(JacksonAutoConfiguration.class)
class PersonControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PersonService personService;

  @MockitoBean
  private CacheManager cacheManager;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void save_ShouldReturnCreatedPerson_WhenDataIsValid() throws Exception {
    // Given
    UUID id = UUID.randomUUID();
    PersonDto personDto = new PersonDto();
    personDto.setName("Jane Doe");
    personDto.setEmail("jane.doe@example.com");

    PersonDto savedPersonDto = new PersonDto();
    savedPersonDto.setId(id);
    savedPersonDto.setName("Jane Doe");
    savedPersonDto.setEmail("jane.doe@example.com");

    when(personService.save(any(PersonDto.class))).thenReturn(savedPersonDto);

    // When & Then
    mockMvc.perform(post("/api/v1/persons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.name").value("Jane Doe"))
        .andExpect(jsonPath("$.email").value("jane.doe@example.com"));
  }

  @Test
  void save_ShouldReturn400_WhenNameIsBlank() throws Exception {
    // Given
    PersonDto personDto = new PersonDto();
    personDto.setName("");
    personDto.setEmail("jane.doe@example.com");

    // When & Then
    mockMvc.perform(post("/api/v1/persons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void save_ShouldReturn400_WhenEmailIsInvalid() throws Exception {
    // Given
    PersonDto personDto = new PersonDto();
    personDto.setName("Jane Doe");
    personDto.setEmail("invalid-email");

    // When & Then
    mockMvc.perform(post("/api/v1/persons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void update_ShouldReturnUpdatedPerson_WhenPersonExists() throws Exception {
    // Given
    UUID id = UUID.randomUUID();
    PersonDto personDto = new PersonDto();
    personDto.setName("John Doe");
    personDto.setEmail("john.doe@example.com");

    PersonDto updatedPersonDto = new PersonDto();
    updatedPersonDto.setId(id);
    updatedPersonDto.setName("John Doe");
    updatedPersonDto.setEmail("john.doe@example.com");

    when(personService.update(eq(id), any(PersonDto.class))).thenReturn(updatedPersonDto);

    // When & Then
    mockMvc.perform(put("/api/v1/persons/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.name").value("John Doe"))
        .andExpect(jsonPath("$.email").value("john.doe@example.com"));
  }

  @Test
  void update_ShouldReturn400_WhenIdIsInvalid() throws Exception {
    // Given
    String invalidId = "not-a-uuid";
    PersonDto personDto = new PersonDto();
    personDto.setName("John Doe");

    // When & Then
    mockMvc.perform(put("/api/v1/persons/{id}", invalidId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(personDto)))
        .andExpect(status().isBadRequest());
  }
}
