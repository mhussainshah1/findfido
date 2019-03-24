package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PetRepository extends CrudRepository<Pet, Long>{
    public List<Pet> findAllByOrderById();

    //todo: implement this method
    List<Pet> findAllByUsers(User user);
}
