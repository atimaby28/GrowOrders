package org.example.groworders.domain.crops.repository;

import org.example.groworders.domain.crops.model.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CropRepository extends JpaRepository<Crop, Long> {
    List<Crop> findByFarmId(Long farmId);
}
