package com.example.restwithspringbootandjava.unittests.mockito.services;


import com.example.restwithspringbootandjava.dto.v1.PersonDTO;
import com.example.restwithspringbootandjava.exceptions.ResourceNotFoundException;
import com.example.restwithspringbootandjava.mapper.PersonMapper;
import com.example.restwithspringbootandjava.model.Person;
import com.example.restwithspringbootandjava.repositories.PersonRepository;
import com.example.restwithspringbootandjava.services.PersonService;
import com.example.restwithspringbootandjava.unittests.mapper.mocks.MockPerson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    MockPerson input;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        input = new MockPerson();
    }

    @Test
    public void testFindById() {

        Person person = input.mockEntity();
        Long id = person.getId();
        PersonDTO personDTO = input.mockDTO();

        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        when(personMapper.personToPersonDTO(person)).thenReturn(personDTO);

        PersonDTO result = personService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getKey());
        verify(personRepository, times(1)).findById(id);
        verify(personMapper, times(1)).personToPersonDTO(person);
    }

    @Test
    public void testFindById_ResourceNotFound() {
        Long id = 1L;

        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personService.findById(id));
        verify(personRepository, times(1)).findById(id);
        verifyNoMoreInteractions(personMapper);
    }

    // @Test
    // public void testFindAll() {
    //     List<Person> personList = input.mockEntityList();

    //     List<PersonDTO> personDTOList = input.mockDTOList();

    //     when(personRepository.findAll()).thenReturn(personList);
    //     when(personMapper.personListToPersonDTOList(personList)).thenReturn(personDTOList);

    //     List<PersonDTO> result = personService.findAll();

    //     assertNotNull(result);
    //     assertEquals(personList.size(), result.size());
    //     verify(personRepository, times(1)).findAll();
    //     verify(personMapper, times(1)).personListToPersonDTOList(personList);
    // }

    @Test
    public void testCreate() {
        Person person = input.mockEntity(1);
        PersonDTO personDTO = input.mockDTO(1);

        when(personMapper.personDTOtoPerson(personDTO)).thenReturn(person);
        when(personMapper.personToPersonDTO(person)).thenReturn(personDTO);
        
        PersonDTO result = personService.create(personDTO);

        assertNotNull(result);
        assertEquals(personDTO.getKey(), result.getKey());

        verify(personMapper, times(1)).personDTOtoPerson(personDTO);
        verify(personMapper, times(1)).personToPersonDTO(person);
        verify(personRepository, times(1)).save(person);
    }

    @Test
    public void testUpdate() {

        Person person = input.mockEntity();
        Long id = person.getId();
        PersonDTO personDTO = input.mockDTO();

        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        when(personMapper.personToPersonDTO(person)).thenReturn(personDTO);

        PersonDTO result = personService.update(personDTO);

        assertNotNull(result);
        assertEquals(personDTO.getKey(), result.getKey());
        verify(personRepository, times(1)).findById(id);
        verify(personMapper, times(1)).personToPersonDTO(person);
        verify(personRepository, times(1)).save(person);
    }

    @Test
    public void testUpdate_ResourceNotFound() {

        PersonDTO personDTO = input.mockDTO();
        Long id = personDTO.getKey();

        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personService.update(personDTO));
        verify(personRepository, times(1)).findById(id);
        verifyNoMoreInteractions(personMapper);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    public void testDelete() {

        Person person = input.mockEntity();
        Long id = person.getId();

        when(personRepository.findById(id)).thenReturn(Optional.of(person));

        personService.delete(id);

        verify(personRepository, times(1)).findById(id);
        verify(personRepository, times(1)).delete(person);
    }

    @Test
    public void testDelete_ResourceNotFound() {
        Long id = 1L;

        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personService.delete(id));
        verify(personRepository, times(1)).findById(id);
        verifyNoMoreInteractions(personRepository);
    }
}
