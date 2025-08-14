package org.example.groworders.domain.farms.service;

import lombok.RequiredArgsConstructor;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.farms.repository.FarmRepository;
import org.example.groworders.domain.users.model.entity.User;
import org.example.groworders.domain.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FarmService {

    private final FarmRepository farmRepository;
    private final UserRepository userRepository;

    // 등록
    public void register(FarmDto.Register dto, Long userId) {
        User user = userRepository.getReferenceById(userId);
        farmRepository.save(dto.toEntity(user));
    }

    // 리스트
    public List<FarmDto.FarmResponse> listAll() {
        List<Farm> result = farmRepository.findAll();
        return result.stream().map(FarmDto.FarmResponse::from).toList();

    }

}
