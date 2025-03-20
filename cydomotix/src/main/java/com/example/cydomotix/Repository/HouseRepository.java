package com.example.cydomotix.Repository;

import com.example.cydomotix.Model.House;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends CrudRepository<House, Integer>{

    //
}
