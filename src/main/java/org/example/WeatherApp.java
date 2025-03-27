package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.*;
import java.net.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.OptionalInt;

public class WeatherApp {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode getWeatherData(String locationName) {
        JsonNode locationData = getLocationData(locationName);
        if (locationData == null || !locationData.has(0)) {
            System.out.println("Error: Location not found.");
            return null;
        }

        JsonNode location = locationData.get(0);
        double latitude = location.get("latitude").asDouble();
        double longitude = location.get("longitude").asDouble();

        String urlString ="https://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="+longitude+
                "&hourly=temperature_2m,weather_code,wind_speed_10m,relative_humidity_2m&timezone=auto";

        try {
            HttpRequest request = getHttpRequest(urlString);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Error: Could not connect to API.");
                return null;
            }

            JsonNode json = objectMapper.readTree(response.body());
            JsonNode hourly = json.get("hourly");

            JsonNode timeArray = hourly.get("time");
            OptionalInt indexOpt = findIndexOfCurrentTime(timeArray);
            if (indexOpt.isEmpty()) return null;

            int index = indexOpt.getAsInt();
            return objectMapper.createObjectNode()
                    .put("temperature", hourly.get("temperature_2m").get(index).asDouble())
                    .put("weatherCode", convertWeatherCode(hourly.get("weather_code").get(index).asLong()))
                    .put("windSpeed", hourly.get("wind_speed_10m").get(index).asDouble())
                    .put("humidity", hourly.get("relative_humidity_2m").get(index).asLong());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JsonNode getLocationData(String locationName) {
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";
        try {

            HttpRequest request = getHttpRequest(urlString);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Error: Could not connect to API.");
                return null;
            }

            return objectMapper.readTree(response.body()).get("results");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpRequest getHttpRequest(String urlString) {
        return HttpRequest.newBuilder().uri(URI.create(urlString)).GET().build();
    }

    private static OptionalInt findIndexOfCurrentTime(JsonNode timeList) {
        String currentTime = getCurrentTime();
        for (int i = 0; i < timeList.size(); i++) {
            if (timeList.get(i).asText().equals(currentTime)) return OptionalInt.of(i);
        }
        return OptionalInt.empty();
    }

    public static String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'"));
    }

    private static final Map<Long, String> WEATHER_MAP = Map.ofEntries(
            Map.entry(0L, "Clear"), Map.entry(1L, "Cloudy"), Map.entry(2L, "Cloudy"), Map.entry(3L, "Cloudy"),
            Map.entry(51L, "Rain"), Map.entry(52L, "Rain"), Map.entry(53L, "Rain"), Map.entry(55L, "Rain"),
            Map.entry(56L, "Rain"), Map.entry(57L, "Rain"), Map.entry(61L, "Rain"), Map.entry(63L, "Rain"),
            Map.entry(65L, "Rain"), Map.entry(66L, "Rain"), Map.entry(67L, "Rain"), Map.entry(80L, "Rain"),
            Map.entry(81L, "Rain"), Map.entry(82L, "Rain"), Map.entry(85L, "Rain"), Map.entry(86L, "Rain"),
            Map.entry(95L, "Rain"), Map.entry(96L, "Rain"), Map.entry(99L, "Rain"),
            Map.entry(71L, "Snow"), Map.entry(72L, "Snow"), Map.entry(73L, "Snow"),
            Map.entry(75L, "Snow"), Map.entry(77L, "Snow")
    );

    private static String convertWeatherCode(long weatherCode) {
        return WEATHER_MAP.getOrDefault(weatherCode, "Unknown");
    }
}