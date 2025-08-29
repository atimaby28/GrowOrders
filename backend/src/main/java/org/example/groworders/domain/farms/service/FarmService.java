package org.example.groworders.domain.farms.service;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import org.example.groworders.config.notification.event.PushEvent;
import org.example.groworders.config.notification.model.entity.NotificationSub;
import org.example.groworders.config.notification.repository.NotificationRepository;
import org.example.groworders.config.notification.service.NotificationService;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.crops.repository.CropRepository;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.farms.repository.FarmQueryRepository;
import org.example.groworders.domain.farms.repository.FarmRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FarmService {
    private final FarmRepository farmRepository;
    private final CropRepository cropRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    //private final UserRepository userRepository;
    //private final ApplicationEventPublisher publisher;


    // 농장 등록
    @Transactional
    public FarmDto.FarmResponse register(FarmDto.Register dto,
                                         @Nullable MultipartFile image,
                                         Long userId) {
        Farm farm = farmRepository.save(dto.toEntity(userId));

        // 테스트용: 농장 등록 시 농부에게 알림 전송
        //publisher.publishEvent(new PushEvent().farmRegisterEvent(userId));

        Optional<NotificationSub> sub = notificationRepository.findById(farm.getUser().getId());


        Notification.builder()
                        .endpoint(sub.en)

        notificationService.send();
        return FarmDto.FarmResponse.from(farm);
    }

    // 농장 정보
    @Transactional(readOnly = true)
    public FarmDto.FarmResponse read(Long id) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("농장을 찾을 수 없습니다."));
        return FarmDto.FarmResponse.from(farm);
    }

    // 농장 정보 수정
    @Transactional
    public FarmDto.FarmResponse update(Long farmId,
                                       FarmDto.Update dto,
                                       @Nullable MultipartFile image,
                                       Long id) {
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new IllegalArgumentException("농장을 찾을 수 없습니다."));

        // 소유자 검증 (권장)
        if (!farm.getUser().getId().equals(id)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }
        dto.update(farm);
        return FarmDto.FarmResponse.from(farm);
    }


    // 농장 리스트
    public List<FarmDto.FarmResponse> listAll() {
        List<Farm> farmList = farmRepository.findAll();
       List<Crop> cropList = cropRepository.findAll().stream().collect(Collectors.toList());

        return farmList.stream().map(FarmDto.FarmResponse::from).toList();
    }




//    // 농장 서치
//    public List<FarmDto.FarmResponse> search(String name) {
//        List<Farm> result = farmRepository.findByName(name);
//        return result.stream().map(FarmDto.FarmResponse::from).toList();
//    }

}
