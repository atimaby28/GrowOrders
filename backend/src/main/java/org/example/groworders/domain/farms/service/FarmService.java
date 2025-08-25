package org.example.groworders.domain.farms.service;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.example.groworders.config.notification.event.Push;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.farms.repository.FarmRepository;
import org.example.groworders.domain.users.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class FarmService {
    private final FarmRepository farmRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    // 농장 등록
    @Transactional
    public FarmDto.FarmResponse register(FarmDto.Register dto,
                                         @Nullable MultipartFile image,
                                         Long userId) {
<<<<<<< HEAD
        Farm farm = farmRepository.save(dto.toEntity(userId));
        //publisher.publishEvent(new Push.FarmRegisterEvent(farm.getId(), farm.getUser().getId()));
=======
        Optional<User> user = userRepository.findById(userId);
        Farm farm = farmRepository.save(dto.toEntity(user.get()));
        publisher.publishEvent(new Push.FarmRegisterEvent(farm.getId(), farm.getUser().getId()));
>>>>>>> 38ab34e529878c3fbd492eb08895579af543e178
        return FarmDto.FarmResponse.from(farm);
    }

//    public FarmDto.FarmResponse register(FarmDto.Register dto,
//                                         @Nullable MultipartFile image) {
//        Farm farm = farmRepository.save(dto.toEntity(dto));
//        return FarmDto.FarmResponse.from(farm);
//    }



    // 리스트
    public List<FarmDto.FarmResponse> listAll() {
        List<Farm> result = farmRepository.findAll();
        return result.stream().map(FarmDto.FarmResponse::from).toList();
    }

    // 농장 서치
    public List<FarmDto.FarmResponse> search(String name) {
        List<Farm> result = farmRepository.findByName(name);
        return result.stream().map(FarmDto.FarmResponse::from).toList();
    }


<<<<<<< HEAD
=======


>>>>>>> 38ab34e529878c3fbd492eb08895579af543e178
}
