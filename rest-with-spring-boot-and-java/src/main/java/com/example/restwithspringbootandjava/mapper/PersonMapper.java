package com.example.restwithspringbootandjava.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.restwithspringbootandjava.dto.v1.PersonDTO;
import com.example.restwithspringbootandjava.model.Person;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(source = "id", target = "key")
    PersonDTO personToPersonDTO(Person person);

    @Mapping(source = "key", target = "id")
    Person personDTOtoPerson(PersonDTO personDTO);

    List<PersonDTO> personListToPersonDTOList(List<Person> personList);
}
