package org.example.groworders.domain.farms.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.users.model.entity.User;

import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private Integer size;
    private String contents;
    private String profile_image_url;
    private String region;

//    selling_count INT DEFAULT 0,
//    review_count INT DEFAULT 0,
//    rating DECIMAL(2,1) DEFAULT 0.0

    // 일대다 (user:farm)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 일대다 (farm:crop)
    @OneToMany(mappedBy = "farm")
    private List<Crop> cropList;
}
