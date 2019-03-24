package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PetRepository extends CrudRepository<Pet, Long>{
    public Iterable<Pet> findAllByOrderById();

    //todo: implement this method
    Iterable<Pet> findAllByUsers(User user);
}
