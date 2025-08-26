package org.example.groworders.domain.crops.repository;

import org.example.groworders.domain.farm.model.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Farm,Integer> {

}
