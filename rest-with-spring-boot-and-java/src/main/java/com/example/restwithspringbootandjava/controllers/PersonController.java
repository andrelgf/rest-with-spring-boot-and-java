package com.example.restwithspringbootandjava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restwithspringbootandjava.dto.v1.PersonDTO;
import com.example.restwithspringbootandjava.services.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/person/v1")
@Tag(name = "People", description = "Endpoint for Managing People")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping()
    @Operation(summary = "finds all people", description = "finds all people", 
    tags = {"People"},
    responses = {
        @ApiResponse(description = "Success", responseCode = "200", 
        content = {
            @Content (
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
            )
        }),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
    })
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "12") Integer size,
        @RequestParam(value = "direction", defaultValue = "asc") String direction
        ) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));

        return ResponseEntity.ok(personService.findAll(pageable));
    }

    @GetMapping(value = "/findPersonsByName/{firstName}")
    @Operation(summary = "finds Persons by first name", description = "finds Persons by first name", 
    tags = {"People"},
    responses = {
        @ApiResponse(description = "Success", responseCode = "200", 
        content = {
            @Content (
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
            )
        }),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
    })
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPersonsByName(
        @PathVariable(value = "firstName") String firstName,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "12") Integer size,
        @RequestParam(value = "direction", defaultValue = "asc") String direction
        ) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));

        return ResponseEntity.ok(personService.findPersonsByName(firstName, pageable));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "finds a person", description = "finds a person", 
    tags = {"People"},
    responses = {
        @ApiResponse(description = "Success", responseCode = "200", 
        content = 
            @Content (
                mediaType = "application/json",
                schema = @Schema(implementation = PersonDTO.class))
            ),
        @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
    })
    public PersonDTO findById(@PathVariable(value = "id") Long id) {
        return personService.findById(id);
    }

    @PostMapping()
    @Operation(summary = "create a person", description = "create a person", 
    tags = {"People"},
    responses = {
        @ApiResponse(description = "Success", responseCode = "200", 
        content = 
            @Content (
                mediaType = "application/json",
                schema = @Schema(implementation = PersonDTO.class))
            ),
        @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
    })
    public PersonDTO create(@RequestBody PersonDTO person) {
        return personService.create(person);
    }

    @PutMapping()
    @Operation(summary = "update a person", description = "update a person", 
    tags = {"People"},
    responses = {
        @ApiResponse(description = "Success", responseCode = "200", 
        content = 
            @Content (
                mediaType = "application/json",
                schema = @Schema(implementation = PersonDTO.class))
            ),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
    })
    public PersonDTO update(@RequestBody PersonDTO person) {
        return personService.update(person);
    }

    @PatchMapping(value = "/{id}")
    @Operation(summary = "Disables person with id", description = "Disables person with id", 
    tags = {"People"},
    responses = {
        @ApiResponse(description = "Success", responseCode = "200", 
        content = 
            @Content (
                mediaType = "application/json",
                schema = @Schema(implementation = PersonDTO.class))
            ),
        @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
    })
    public PersonDTO disablePerson(@PathVariable(value = "id") Long id) {
        return personService.disablePerson(id);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "delete a person", description = "delete a person", 
    tags = {"People"},
    responses = {
        @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
    })
    public void delete(@PathVariable(value = "id") Long id) {
        personService.delete(id);
    }
}
