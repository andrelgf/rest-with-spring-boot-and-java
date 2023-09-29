package com.example.restwithspringbootandjava.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.restwithspringbootandjava.model.Person;
import com.example.restwithspringbootandjava.services.PersonService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping()
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Person findById(@PathVariable(value = "id") Long id) {
        return personService.findById(id);
    }

    @PostMapping()
    public Person create(@RequestBody Person person) {
        return personService.create(person);
    }

    @PutMapping()
    public Person update(@RequestBody Person person) {
        return personService.update(person);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        personService.delete(id);
    }
}
