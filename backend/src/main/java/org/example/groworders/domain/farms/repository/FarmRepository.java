package org.example.groworders.domain.farms.repository;

import org.example.groworders.domain.farms.model.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmRepository extends JpaRepository<Farm, Long> {
}
