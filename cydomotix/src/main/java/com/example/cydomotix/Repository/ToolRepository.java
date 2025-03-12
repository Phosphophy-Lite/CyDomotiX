package com.example.cydomotix.Repository;

import com.example.cydomotix.Model.Tool;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends CrudRepository<Tool, Long> {

    //

}
