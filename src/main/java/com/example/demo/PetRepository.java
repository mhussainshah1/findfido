package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface PetRepository extends CrudRepository<Pet, Long> {
    Iterable<Pet> findAllByOrderById();

    //todo: implement this method
    Iterable<Pet> findAllByUsers(User user);
}
