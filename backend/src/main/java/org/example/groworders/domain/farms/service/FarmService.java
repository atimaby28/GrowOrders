package org.example.groworders.domain.farms.service;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.groworders.config.notification.model.entity.NotificationSub;
import org.example.groworders.config.notification.repository.NotificationRepository;
import org.example.groworders.config.notification.service.NotificationService;
import org.example.groworders.domain.crops.model.entity.Crop;
import org.example.groworders.domain.crops.repository.CropRepository;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.farms.repository.FarmRepository;
import org.example.groworders.domain.users.service.S3UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FarmService {
    private final FarmRepository farmRepository;
    private final CropRepository cropRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final S3UploadService s3UploadService;
    //private final ApplicationEventPublisher publisher;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String s3BucketName;

    // 농장 등록
    @Transactional
    public FarmDto.FarmResponse register(FarmDto.Register dto,
                                         @Nullable MultipartFile farmImageUrl,
                                         Long userId) {
        // 농장 이미지 업로드
        String filePath = s3UploadService.upload(farmImageUrl);
        Farm farm = farmRepository.save(dto.toEntity(userId, filePath));

        // 테스트용: 농장 등록 시 농부에게 알림 전송
        //publisher.publishEvent(new PushEvent().farmRegisterEvent(userId));
        //Optional<NotificationSub> sub = notificationRepository.findById(farm.getUser().getId());
        //notificationService.send();

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
        return farmList.stream().map(FarmDto.FarmResponse::from).toList();
    }
}
