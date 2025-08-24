package org.example.groworders.domain.users.service;

import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.groworders.common.exception.*;
import org.example.groworders.domain.users.model.dto.EmailVerify;
import org.example.groworders.domain.users.model.dto.UserDto;
import org.example.groworders.domain.users.model.entity.User;
import org.example.groworders.domain.users.repository.EmailVerifyRepository;
import org.example.groworders.domain.users.repository.UserRepository;
import org.example.groworders.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JavaMailSender emailSender;
    private final EmailVerifyRepository emailVerifyRepository;
    private final S3Operations s3Operations;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String s3BucketName;

    @Transactional
    public void signup(UserDto.SignUp dto, MultipartFile profileImageUrl) {
        // 프로필 업로드
        String filePath = upload(profileImageUrl);

        // 회원 저장
        User user = dto.toEntity(filePath);
        user.passwordEncrypted(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        // 이메일 전송
        String uuid = UUID.randomUUID().toString();
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(dto.getEmail());
            helper.setSubject("[내 사이트] 가입 환영");
            String htmlContent = "<h2 style='color: #2e6c80;'>가입을 환영합니다!</h2>"
                    + "<p>아래 링크를 클릭하여 이메일 인증을 완료해주세요:</p>"
                    + "<a href='http://localhost:8080/users/verify?uuid=" + uuid + "'>이메일 인증하기</a>";
            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendException("가입 환영 이메일 전송 실패", e);
        }

        // 이메일 인증 UUID 저장
        EmailVerify emailVerify = EmailVerify.builder()
                .uuid(uuid)
                .user(user)
                .build();
        emailVerifyRepository.save(emailVerify);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(UserDto.AuthUser::from)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일의 사용자를 찾을 수 없습니다."));
    }

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("프로필 이미지를 선택해주세요.");
        }

        try {
            String dirPath = FileUploadUtil.makeUploadPath();
            S3Resource s3Resource = s3Operations.upload(s3BucketName, dirPath + file.getOriginalFilename(), file.getInputStream());
            return s3Resource.getURL().toString();
        } catch (IOException e) {
            throw new FileUploadException("프로필 이미지 업로드 실패", e);
        }
    }

    @Transactional
    public void verify(String uuid) {
        EmailVerify emailVerify = emailVerifyRepository.findByUuid(uuid)
                .orElseThrow(() -> new EmailVerifyException("이메일 인증에 실패했습니다."));

        User user = emailVerify.getUser();
        user.userVerify();
        userRepository.save(user);
    }
}
