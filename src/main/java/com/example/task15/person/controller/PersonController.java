package com.example.task15.person.controller;

import com.example.task15.person.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {
    private List<Person> persons = new ArrayList<>(Arrays.asList(
            new Person(1, "Ivan", "Ivanovich", "Ivanov", LocalDate.of(1999, 2,3)),
            new Person(2, "Петр", "Петрович", "Петров", LocalDate.of(2002, 2,2)),
            new Person(3, "Евгений", "Васильевич", "Васин", LocalDate.of(2005, 4,8)),
            new Person(4, "Максим", "Яковлевич", "Окопский", LocalDate.of(1978, 6,5))
    ));

    // Get requests
    @GetMapping("/persons")
    public Iterable<Person> getPersons() {
        return persons;
    }

    @GetMapping("/person/{id}")
    public Optional<Person> findPersonById(@PathVariable int id) {
        return persons.stream().filter(p -> p.getId() == id).findAny();
    }



    // Post requests
    @PostMapping("/persons")
    public ResponseEntity<Object> addPerson(@RequestBody Person person) {
        int newId = persons.stream().mapToInt(Person::getId).max().orElse(0) + 1;
        person.setId(newId);
        persons.add(person);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }


    // Put requests
    @PutMapping("/persons/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person person) {
        int index = -1;
        for (Person p : persons) {
            if (p.getId() == id) {
                index = persons.indexOf(p);
                break;
            }
        }

        if (index == -1) {
            Person newPerson = new Person(id, person.getFirstname(), person.getSurname(), person.getLastname(), person.getBirthday());
            persons.add(newPerson);
            return new ResponseEntity<>(newPerson, HttpStatus.CREATED);
        } else {
            Person updatedPerson = new Person(id, person.getFirstname(), person.getSurname(), person.getLastname(), person.getBirthday());
            persons.set(index, updatedPerson);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        }
    }


    // Delete requests
    @DeleteMapping("/persons/{id}")
    public void deletePerson(@PathVariable int id) {
        persons.removeIf(p -> p.getId() == id);
    }

}