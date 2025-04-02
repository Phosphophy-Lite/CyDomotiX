package com.example.cydomotix.Repository.Objects;

import com.example.cydomotix.Model.Objects.ConnectedObject;
import com.example.cydomotix.Model.Objects.Mode;
import com.example.cydomotix.Model.Objects.Connectivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectedObjectRepository extends ListCrudRepository<ConnectedObject, Integer> {
    Optional<ConnectedObject> findByName(String name);

    @Query("""
    SELECT DISTINCT co
    FROM ConnectedObject co
    LEFT JOIN co.objectType ot
    LEFT JOIN co.room ro
    LEFT JOIN ObjectAttribute oa ON ot.id = oa.objectType.id
    LEFT JOIN AttributeValue av ON co.id = av.connectedObject.id AND av.objectAttribute.id = oa.id
    WHERE
        (:keyword IS NULL OR LOWER(co.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(ot.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(ro.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(co.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(oa.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(av.string_value) LIKE LOWER(CONCAT('%', :keyword, '%')))
    AND (:objectTypeId IS NULL OR ot.id = :objectTypeId)
    AND (:roomId IS NULL OR ro.id = :roomId)
    AND (:brand IS NULL OR LOWER(co.brand) LIKE LOWER(CONCAT('%', :brand, '%')))
    AND (:mode IS NULL OR co.mode = :mode)
    AND (:connectivity IS NULL OR co.connectivity = :connectivity)
""")
    List<ConnectedObject> searchObjects(
            @Param("keyword") String keyword,
            @Param("objectTypeId") Integer objectTypeId,
            @Param("roomId") Integer roomId,
            @Param("brand") String brand,
            @Param("mode") Mode mode,
            @Param("connectivity") Connectivity connectivity
    );


}
