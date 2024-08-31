package com.example.greenproject.repository;

import com.example.greenproject.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {
    @Query(nativeQuery = true,value = "SELECT * FROM _image i WHERE i.product_id =:id")
    List<Image> getAllByProductId(@Param("id") Long id);
}
