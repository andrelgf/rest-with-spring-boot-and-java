package com.example.restwithspringbootandjava.unittests.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import com.example.restwithspringbootandjava.dto.v1.PersonDTO;
import com.example.restwithspringbootandjava.mapper.PersonMapper;
import com.example.restwithspringbootandjava.model.Person;
import com.example.restwithspringbootandjava.unittests.mapper.mocks.MockPerson;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersonMapperTest {

    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);
    MockPerson input;

    @BeforeEach
    void setUpMocks() {
        input = new MockPerson();
    }

    @Test
    public void testPersonToPersonDTO() {

        Person person = input.mockEntity();

        PersonDTO personDTO = personMapper.personToPersonDTO(person);

        assertNotNull(personDTO);
        assertEquals(person.getId(), personDTO.getKey());
        assertEquals(person.getFirstName(), personDTO.getFirstName());
    }

    @Test
    public void testPersonDTOtoPerson() {

        PersonDTO personDTO = input.mockDTO();

        Person person = personMapper.personDTOtoPerson(personDTO);

        assertNotNull(person);
        assertEquals(personDTO.getKey(), person.getId());
        assertEquals(personDTO.getFirstName(), person.getFirstName());
    }

    @Test
    public void testPersonListToPersonDTOList() {

        List<Person> personList = input.mockEntityList();


        List<PersonDTO> personDTOList = personMapper.personListToPersonDTOList(personList);

        assertNotNull(personDTOList);
        assertEquals(personList.size(), personDTOList.size());

        for (int i = 0; i < personList.size(); i++) {
            assertEquals(personList.get(i).getId(), personDTOList.get(i).getKey());
            assertEquals(personList.get(i).getFirstName(), personDTOList.get(i).getFirstName());
        }
    }
}
