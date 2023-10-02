package com.example.restwithspringbootandjava.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.restwithspringbootandjava.dto.v1.PersonDTO;
import com.example.restwithspringbootandjava.model.Person;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDTO personToPersonDTO(Person person);

    Person personDTOtoPerson(PersonDTO personDTO);

    List<PersonDTO> personListToPersonDTOList(List<Person> personList);
}
