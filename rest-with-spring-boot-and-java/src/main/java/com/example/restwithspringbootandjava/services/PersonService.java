package com.example.restwithspringbootandjava.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import com.example.restwithspringbootandjava.controllers.PersonController;
import com.example.restwithspringbootandjava.dto.v1.PersonDTO;
import com.example.restwithspringbootandjava.exceptions.ResourceNotFoundException;
import com.example.restwithspringbootandjava.mapper.PersonMapper;
import com.example.restwithspringbootandjava.repositories.PersonRepository;



@Service
public class PersonService {


    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonMapper personMapper;

    public PersonDTO findById(Long id){
        logger.info("finding person with Id: " + id);
        var entity = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("no records found for this ID"));
        var personDto = personMapper.personToPersonDTO(entity);
        personDto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personDto;
    }

    public List<PersonDTO> findAll(){
        logger.info("listing all people");
        var persons = personMapper.personListToPersonDTOList(personRepository.findAll());
        persons.
        stream()
        .forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return persons;
    }

    public PersonDTO create(PersonDTO person){
        logger.info("creating person with name: " + person.getFirstName());
        var entity = personMapper.personDTOtoPerson(person);
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

    public void delete(Long id){
        logger.info("deleting person with Id: " + id);
        var entity = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("no records found for this ID"));
        personRepository.delete(entity);
    }

}
