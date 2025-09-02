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
import org.example.groworders.domain.users.repository.UserRepository;
import org.example.groworders.domain.users.service.S3PresignedUrlService;
import org.springframework.context.ApplicationEventPublisher;
import org.example.groworders.domain.users.service.S3UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FarmService {
    private final FarmRepository farmRepository;
    private final CropRepository cropRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final S3UploadService s3UploadService; //업로드
    private final S3PresignedUrlService s3PresignedUrlService; //불러오기
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

/* 현경 코드
* Farm farm = farmRepository.save(dto.toEntity(userId));
        //publisher.publishEvent(new Push.FarmRegisterEvent(farm.getId(), farm.getUser().getId()));

        String presignedUrl = farm.getProfile_image_url() != null
                ? s3PresignedUrlService.generatePresignedUrl(farm.getProfile_image_url(), Duration.ofMinutes(60)) : null;

        return FarmDto.FarmResponse.from(farm, presignedUrl);
*
* */

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
    public List<FarmDto.FarmListResponse> listAll() {
        List<Farm> farmList = farmRepository.findAll();
        return farmList.stream().map(farm -> {
            String presignedUrl = farm.getFarmImage() != null ?
                    s3PresignedUrlService.generatePresignedUrl(farm.getFarmImage(), Duration.ofMinutes(60)) :
                    s3PresignedUrlService.generatePresignedUrl("not-found-image.jpg", Duration.ofMinutes(60));
            return FarmDto.FarmListResponse.from(farm, presignedUrl);
        }).toList();

        /**
         * return result.stream()
         *             .map(farm -> {
         *                 String presignedUrl = s3PresignedUrlService.generatePresignedUrl(
         *                         farm.getProfileImagePath(), // 파일 경로나 키
         *                         Duration.ofMinutes(10)      // URL 유효기간
         *                 );
         *                 return FarmDto.FarmResponse.from(farm, presignedUrl);
         *             })
         *             .toList();
         *             */

/* 소민 코드
        List<Farm> farmList = farmRepository.findAll();
        return farmList.stream().map(FarmDto.FarmResponse::from).toList();
*/
    }

//    // 농장 서치
//    public List<FarmDto.FarmResponse> search(String name) {
//        List<Farm> result = farmRepository.findByName(name);
//        return result.stream().map(FarmDto.FarmResponse::from).toList();
//    }

}
