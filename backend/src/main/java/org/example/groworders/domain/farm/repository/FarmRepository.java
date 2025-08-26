package org.example.groworders.domain.farm.repository;

import org.example.groworders.domain.farm.model.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmRepository extends JpaRepository<Farm,Integer> {

}
