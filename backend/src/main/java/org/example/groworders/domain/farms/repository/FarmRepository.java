package org.example.groworders.domain.farms.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> 38ab34e529878c3fbd492eb08895579af543e178

@Repository
public interface FarmRepository extends JpaRepository<Farm, Integer> {
    List<Farm> findByName(String name);

<<<<<<< HEAD
    Optional<Farm> findById(@NotNull(message = "농장은 필수 선택입니다.") @Positive(message = "농장을 확인 해주세요.") Long farmId);
=======
>>>>>>> 38ab34e529878c3fbd492eb08895579af543e178
}
