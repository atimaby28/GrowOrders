package org.example.groworders.domain.farms.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.groworders.domain.crops.model.Crop;
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
    private String name; //농장 이름
    private String description; //농장 설명

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; //농장을 소유한 사용자 : 외래키

    @OneToMany(mappedBy = "farm")
    private List<Crop> crops; //소유한 작물
}
