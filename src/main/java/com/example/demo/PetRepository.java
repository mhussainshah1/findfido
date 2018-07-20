package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PetRepository extends CrudRepository<Pet, Long>{
    public List<Pet> findAllByOrderById();
    List<Pet> findAllByUser(User user);
}
