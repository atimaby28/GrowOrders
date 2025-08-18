package org.example.groworders.domain.farms.service;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.farms.repository.FarmRepository;
import org.example.groworders.domain.users.model.entity.User;
import org.example.groworders.domain.users.repository.UserRepository;
import org.example.groworders.utils.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FarmService {

    private final FarmRepository farmRepository;
    private final UserRepository userRepository;

    // 등록
    public FarmDto.FarmResponse register(FarmDto.Register dto,
                                         @Nullable MultipartFile image) {
        Farm farm = farmRepository.save(dto.toEntity(dto));
        return FarmDto.FarmResponse.from(farm);
    }


//    public FarmDto.FarmResponse register(FarmDto.Register dto,
//                                         @Nullable MultipartFile image,
//                                         Long userId) {
//        User user = userRepository.getReferenceById(userId);
//        Farm farm = farmRepository.save(dto.toEntity(user));
//        return FarmDto.FarmResponse.from(farm);
//    }

    // 리스트
    public List<FarmDto.FarmResponse> listAll() {
        List<Farm> result = farmRepository.findAll();
        return result.stream().map(FarmDto.FarmResponse::from).toList();

    }

}
