package com.example.restwithspringbootandjava.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.example.restwithspringbootandjava.controllers.PersonController;
import com.example.restwithspringbootandjava.dto.v1.PersonDTO;
import com.example.restwithspringbootandjava.exceptions.ResourceNotFoundException;
import com.example.restwithspringbootandjava.mapper.PersonMapper;
import com.example.restwithspringbootandjava.repositories.PersonRepository;

import jakarta.transaction.Transactional;



@Service
public class PersonService {


    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private PagedResourcesAssembler<PersonDTO> pagedResourcesAssembler;

    public PersonDTO findById(Long id){
        logger.info("finding person with Id: " + id);
        var entity = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("no records found for this ID"));
        var personDto = personMapper.personToPersonDTO(entity);
        personDto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personDto;
    }

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable){
        logger.info("listing all people");

        var personPage = personRepository.findAll(pageable);
        
        var personDtoPage = personPage.map(p -> personMapper.personToPersonDTO(p));
        personDtoPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        
        Link link = linkTo(
            methodOn(PersonController.class).findAll(
                pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return pagedResourcesAssembler.toModel(personDtoPage, link);
    }

    public PagedModel<EntityModel<PersonDTO>> findPersonsByName(String firstName, Pageable pageable){
        logger.info("listing all people");

        var personPage = personRepository.findPersonsByName(firstName, pageable);
        
        var personDtoPage = personPage.map(p -> personMapper.personToPersonDTO(p));
        personDtoPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        
        Link link = linkTo(
            methodOn(PersonController.class).findAll(
                pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return pagedResourcesAssembler.toModel(personDtoPage, link);
    }

    public PersonDTO create(PersonDTO person){
        logger.info("creating person with name: " + person.getFirstName());
        var entity = personMapper.personDTOtoPerson(person);
        personRepository.save(entity);
        var personDto = personMapper.personToPersonDTO(entity);
        personDto.add(linkTo(methodOn(PersonController.class).findById(personDto.getKey())).withSelfRel());
        return personDto;
    }

    public PersonDTO update(PersonDTO person){
        logger.info("updating person with Id: " + person.getKey());

        var entity = personRepository.findById(person.getKey())
        .orElseThrow(() -> new ResourceNotFoundException("no records found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        personRepository.save(entity);
        var personDto = personMapper.personToPersonDTO(entity);
        personDto.add(linkTo(methodOn(PersonController.class).findById(personDto.getKey())).withSelfRel());
        return personDto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id){
        logger.info("Disabling person with id: " + id);
        personRepository.disablePerson(id);
        var entity = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("no records found for this ID"));
        var personDto = personMapper.personToPersonDTO(entity);
        personDto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personDto;
    }

    public void delete(Long id){
        logger.info("deleting person with Id: " + id);
        var entity = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("no records found for this ID"));
        personRepository.delete(entity);
    }

}
