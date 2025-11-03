package com.nisarg.spring.demorest.mapper;

import com.nisarg.spring.demorest.dto.PersonDto;
import com.nisarg.spring.demorest.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PersonMapper {

  PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

  // example method to map Person to Person (identity)
  Person toEntity(PersonDto person);

  // add other mapping methods as needed, e.g. Person to PersonDTO
  PersonDto toDto(Person person);
}
