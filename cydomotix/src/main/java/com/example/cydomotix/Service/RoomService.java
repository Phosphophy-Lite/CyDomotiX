package com.example.cydomotix.Service;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Room;
import com.example.cydomotix.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Récupère toutes les pièces enregistrées dans la BDD
     * @return
     */
    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }

    /**
     * Récupère une pièce via son id
     * @param id
     * @return
     */
    public Room getRoomById(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room with id " + id + " does not exist."));
        return room;
    }
}
