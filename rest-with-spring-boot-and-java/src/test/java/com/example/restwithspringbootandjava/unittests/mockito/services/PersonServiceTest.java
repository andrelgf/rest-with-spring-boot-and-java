package com.example.restwithspringbootandjava.unittests.mockito.services;

import com.example.restwithspringbootandjava.controllers.PersonController;
import com.example.restwithspringbootandjava.dto.v1.PersonDTO;
import com.example.restwithspringbootandjava.exceptions.ResourceNotFoundException;
import com.example.restwithspringbootandjava.mapper.PersonMapper;
import com.example.restwithspringbootandjava.model.Person;
import com.example.restwithspringbootandjava.repositories.PersonRepository;
import com.example.restwithspringbootandjava.services.PersonService;
import com.example.restwithspringbootandjava.unittests.mapper.mocks.MockPerson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private PagedResourcesAssembler<PersonDTO> pagedResourcesAssembler;

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

    @Test
    public void testFindAll() {
        // Mock data
        Pageable pageable = mock(Pageable.class);
        List<Person> personList = input.mockEntityList();
        Page<Person> personPage = new PageImpl<>(personList);
    
        when(personRepository.findAll(pageable)).thenReturn(personPage);
    
        // Mock mapping
        List<PersonDTO> personDTOList = input.mockDTOList();
        when(personMapper.personToPersonDTO(any())).thenReturn(personDTOList.get(0));
    
        // Mock link creation
        Link selfLink = linkTo(
            methodOn(PersonController.class).findAll(
                pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
    
        // Mocking PagedModel<EntityModel<PersonDTO>>
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
            pageable.getPageSize(), personPage.getNumber(), personPage.getTotalElements());
        List<EntityModel<PersonDTO>> entityModelList = new ArrayList<>();
        
        for (PersonDTO personDTO : personDTOList) {
            EntityModel<PersonDTO> entityModel = EntityModel.of(personDTO);
            entityModel.add(selfLink); // Adding self link to each entity model
            entityModelList.add(entityModel);
        }
    
        PagedModel<EntityModel<PersonDTO>> mockedPagedModel = PagedModel.of(
            entityModelList, metadata, selfLink);
    
        when(pagedResourcesAssembler.toModel(
                any(Page.class), 
                any(Link.class)))
            .thenReturn(mockedPagedModel);
    
        // Call the method
        PagedModel<EntityModel<PersonDTO>> result = personService.findAll(pageable);
    
        // Assertions or verifications based on your specific logic
        assertNotNull(result); // Add assertions as needed based on your specific logic
    
        // Verify that the repository method was called
        verify(personRepository, times(1)).findAll(pageable);
    
        // Verify that the mapping method was called
        verify(personMapper, times(personList.size())).personToPersonDTO(any());
    
        // Verify that the link creation method was called
        verify(pagedResourcesAssembler, times(1))
            .toModel(any(Page.class), any(Link.class));
    
        // Add assertions as needed based on your specific logic
    }
    

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
