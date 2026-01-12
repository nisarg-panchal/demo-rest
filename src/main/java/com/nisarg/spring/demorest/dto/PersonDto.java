package com.nisarg.spring.demorest.dto;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static lombok.AccessLevel.PRIVATE;

import com.nisarg.spring.demorest.entity.Person;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link Person}.
 *
 * <p>Represents the API-facing view of a person. Used as request/response payload in REST
 * endpoints.
 */
@Schema(
    name = "Person",
    description = "API representation of a person.",
    accessMode = READ_WRITE
)
@FieldDefaults(level = PRIVATE)
@Data
public class PersonDto implements Serializable {

  @Schema(
      description = "Unique identifier of the person.",
      example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      format = "uuid",
      accessMode = READ_ONLY
  )
  UUID id;

  @NotBlank
  @Size(max = 200)
  @Schema(
      description = "Full name of the person.",
      example = "Nisarg Panchal",
      minLength = 1,
      maxLength = 200,
      requiredMode = REQUIRED
  )
  String name;

  @Email
  @Size(max = 320)
  @Schema(
      description = "Email address used for contacting the person.",
      example = "nisarg@example.com",
      format = "email",
      maxLength = 320,
      requiredMode = NOT_REQUIRED
  )
  String email;
}
