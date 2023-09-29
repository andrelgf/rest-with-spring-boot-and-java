package com.example.restwithspringbootandjava.repositories;

import org.springframework.stereotype.Repository;

import com.example.restwithspringbootandjava.model.Person;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
    
}
