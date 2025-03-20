package com.example.cydomotix.Repository;

import com.example.cydomotix.Model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room, Integer>{

    //
}
