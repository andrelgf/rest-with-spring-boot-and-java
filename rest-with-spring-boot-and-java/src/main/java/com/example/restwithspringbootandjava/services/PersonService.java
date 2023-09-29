package com.example.restwithspringbootandjava.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restwithspringbootandjava.exceptions.ResourceNotFoundException;
import com.example.restwithspringbootandjava.model.Person;
import com.example.restwithspringbootandjava.repositories.PersonRepository;



@Service
public class PersonService {


    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;

    public Person findById(Long id){
        logger.info("finding person with Id: " + id);
        return personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("no records found for this ID"));
    }

    public List<Person> findAll(){
        logger.info("listing all people");
        return personRepository.findAll();
    }

    public Person create(Person person){
        logger.info("creating person with name: " + person.getFirstName());
        return personRepository.save(person);
    }

    public Person update(Person person){
        logger.info("updating person with Id: " + person.getId());

        var entity = personRepository.findById(person.getId())
        .orElseThrow(() -> new ResourceNotFoundException("no records found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return personRepository.save(entity);
    }

    public void delete(Long id){
        logger.info("deleting person with Id: " + id);
        var entity = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("no records found for this ID"));
        personRepository.delete(entity);
    }

}
