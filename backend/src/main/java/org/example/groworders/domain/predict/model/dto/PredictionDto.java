package org.example.groworders.domain.predict.model.dto;

import lombok.*;
import org.example.groworders.domain.weather.model.entity.Weather;

import java.util.List;

public class PredictionDto {

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestDaily {
        private String cropName;
        private String cultivationType;
        private String growthStage;
        private WeatherData weatherData;
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestYearly {
        private String cropName;
        private String cultivationType;
        private String growthStage;
        private List<WeatherData> weatherDataList;
    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherData {
        private String tm;
        private String stn;
        private String ws;
        private String ta;
        private String hm;
        private String rn;
        private String si;

        // 엔티티 → DTO 변환
        public static WeatherData fromEntity(Weather entity) {
            return WeatherData.builder()
                    .tm(entity.getTm())
                    .stn(entity.getStn())
                    .ws(entity.getWs())
                    .ta(entity.getTa())
                    .hm(entity.getHm())
                    .rn(entity.getRn())
                    .si(entity.getSi())
                    .build();
        }

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String cropName;
        private String cultivateType;
        private String growthStage;
        private BestMatch bestMatch;
        private String predictedYield;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BestMatch {
        private String tm;
        private String stn;
        private Double taMax;
        private Double taMin;
        private Double siMax;
        private Double siMin;
        private Double yield;
    }
}
