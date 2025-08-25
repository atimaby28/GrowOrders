package org.example.groworders.config.notification.service;

import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.example.groworders.config.notification.model.dto.NotificationDto;
import org.example.groworders.config.notification.model.entity.NotificationSub;
import org.example.groworders.config.notification.repository.NotificationRepository;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class NotificationService {
    private final PushService pushService;
    private final NotificationRepository notificationRepository;
    public NotificationService(NotificationRepository repo) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        this.notificationRepository = repo;
        if (Security.getProperty(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        this.pushService = new PushService();
        this.pushService.setPublicKey("BLaZmJ4WyIM5yOg3-xMWtvsLEvKQGFM5OYog7hwniwpLKQifl8hVof-knJsFSTwGcpCFrDmeqNMlE3yNgO122gc");
        this.pushService.setPrivateKey("EVhrl9c6GsT4UCzcLZdSPPHppezt6N2XJm1z4e2e0AQ");
        this.pushService.setSubject("우리 서버");
    }

    public void subscribe(NotificationDto.Subscribe dto, Long id) {
        log.debug(dto.getEndpoint());
        log.debug(dto.getKeys().getP256dh());
        log.debug(dto.getKeys().getAuth());

        NotificationSub notificationSub = NotificationSub.builder()
                .endpoint(dto.getEndpoint())
                .keys(dto.getKeys())
                .userId(id)
                .build();
        notificationRepository.save(notificationSub);
    }



    public void send(NotificationSub dto) throws GeneralSecurityException, JoseException, IOException, ExecutionException, InterruptedException {
        // 사용자에게 메시지 전송
        Subscription.Keys keys = new Subscription.Keys(
                dto.getKeys().getP256dh(),
                dto.getKeys().getAuth()
        );

        Subscription subscription = new Subscription(dto.getEndpoint(), keys);
        String payload = "{\"title\":\"📬 새 알림\",\"message\":\"푸시 메시지 도착!\"}";
        Notification notification = new Notification(subscription, payload);
        HttpResponse response = pushService.send(notification);
        String body = response.getEntity() != null
                ? org.apache.http.util.EntityUtils.toString(response.getEntity())
                : "";
        log.debug("STATUS={}", response.getStatusLine());
        log.debug("BODY={}", body);
    }
}
