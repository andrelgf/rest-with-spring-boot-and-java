package com.example.restwithspringbootandjava.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return personMapper.personToPersonDTO(entity);
    }

    public List<PersonDTO> findAll(){
        logger.info("listing all people");
        return personMapper.personListToPersonDTOList(personRepository.findAll());
    }

    public PersonDTO create(PersonDTO person){
        logger.info("creating person with name: " + person.getFirstName());
        var entity = personMapper.personDTOtoPerson(person);
        personRepository.save(entity);
        return personMapper.personToPersonDTO(entity);
    }

    public PersonDTO update(PersonDTO person){
        logger.info("updating person with Id: " + person.getId());

        var entity = personRepository.findById(person.getId())
        .orElseThrow(() -> new ResourceNotFoundException("no records found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        personRepository.save(entity);

        return personMapper.personToPersonDTO(entity);
    }

    public void delete(Long id){
        logger.info("deleting person with Id: " + id);
        var entity = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("no records found for this ID"));
        personRepository.delete(entity);
    }

}
