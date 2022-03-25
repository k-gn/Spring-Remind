package com.example.springcore.objectmapper;

import com.example.springcore.objectmapper.dto.Car;
import com.example.springcore.objectmapper.dto.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String args[]) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        User user = new User();
        user.setAge(10);
        user.setName("홍길동");

        Car car1 = new Car();
        car1.setName("AUDI");
        car1.setNumber("1111");

        Car car2 = new Car();
        car2.setName("BMW");
        car2.setNumber("2222");

        List<Car> carList = List.of(car1, car2);
        user.setCar(carList);

        // json 의 기본 인코딩은 UTF-8
        String json = objectMapper.writeValueAsString(user);
        System.out.println(json);

        User parsing = objectMapper.readValue(json, User.class);
        System.out.println(parsing);

        // node parsing
        JsonNode jsonNode = objectMapper.readTree(json);
        String name = jsonNode.get("name").asText();
        int age = jsonNode.get("age").asInt();
        System.out.println(name);
        System.out.println(age);

        JsonNode cars = jsonNode.get("car");
        ArrayNode arrayNode = (ArrayNode) cars;
        List<Car> parseCars = objectMapper.convertValue(arrayNode, new TypeReference<>() {});
        System.out.println(parseCars);

        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.put("name","김길동"); // 수정
        System.out.println(objectNode.toPrettyString());

        // 위 내용들을 활용해 원하는 형태로 json 을 다룰 수 있다.
    }
}
