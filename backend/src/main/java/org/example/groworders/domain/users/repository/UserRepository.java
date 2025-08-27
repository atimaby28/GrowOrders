package org.example.groworders.domain.users.repository;

import org.example.groworders.domain.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    User getReferenceById(Long userId);

    /**
     * select farm.id from user
     * left join farm on user.id = farm.user_id;
     * */
    @Query("SELECT f.id FROM User u LEFT JOIN u.farmList f WHERE u.id = :userId ")
    List<Long> findByIdWithFarm(Long userId);
}
