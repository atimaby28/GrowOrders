package org.example.groworders.domain.weather.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class WeatherDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObservationDto {
        private String tm;   // 관측시각
        private String stn;  // 지점번호
        private String ws;   // 풍속
        private String ta;   // 기온
        private String hm;   // 습도
        private String rn;   // 강수량
        private String si;   // 일사량
    }
}