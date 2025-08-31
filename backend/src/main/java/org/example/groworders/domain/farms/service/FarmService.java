package org.example.groworders.domain.farms.service;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.example.groworders.config.notification.event.Push;
import org.example.groworders.domain.farms.model.dto.FarmDto;
import org.example.groworders.domain.farms.model.entity.Farm;
import org.example.groworders.domain.farms.repository.FarmRepository;
import org.example.groworders.domain.users.repository.UserRepository;
import org.example.groworders.domain.users.service.S3PresignedUrlService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class FarmService {
    private final FarmRepository farmRepository;
    //private final UserRepository userRepository;
    //private final ApplicationEventPublisher publisher;
    private final S3PresignedUrlService s3PresignedUrlService;


    // 농장 등록
    @Transactional
    public FarmDto.FarmResponse register(FarmDto.Register dto,
                                         @Nullable MultipartFile image,
                                         Long userId) {
        Farm farm = farmRepository.save(dto.toEntity(userId));
        //publisher.publishEvent(new Push.FarmRegisterEvent(farm.getId(), farm.getUser().getId()));

        String presignedUrl = farm.getProfile_image_url() != null
                ? s3PresignedUrlService.generatePresignedUrl(farm.getProfile_image_url(), Duration.ofMinutes(60)) : null;

        return FarmDto.FarmResponse.from(farm, presignedUrl);
    }


    // 리스트
    public List<FarmDto.FarmResponse> listAll() {
        List<Farm> result = farmRepository.findAll();
//        return result.stream().map(farm -> FarmDto.FarmResponse::from).toList();
        return result.stream().map(farm -> {
            String presignedUrl = farm.getProfile_image_url() != null ?
                    s3PresignedUrlService.generatePresignedUrl(farm.getProfile_image_url(), Duration.ofMinutes(60)) :
                    s3PresignedUrlService.generatePresignedUrl("not-found-image.jpg", Duration.ofMinutes(60));
            return FarmDto.FarmResponse.from(farm, presignedUrl);
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
    }

//    // 농장 서치
//    public List<FarmDto.FarmResponse> search(String name) {
//        List<Farm> result = farmRepository.findByName(name);
//        return result.stream().map(FarmDto.FarmResponse::from).toList();
//    }

}
