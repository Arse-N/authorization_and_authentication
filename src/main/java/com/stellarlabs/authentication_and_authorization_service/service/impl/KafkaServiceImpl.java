//package com.stellarlabs.authentication_and_authorization_service.service.impl;
//
//import com.stellarlabs.authentication_and_authorization_service.service.KafkaService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class KafkaServiceImpl implements KafkaService {
//
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//
//    @Override
//    public void sendToKafka(String topic, Object data) {
//
//        kafkaTemplate.send(topic, data);
//        System.out.println("tpiiii uxarkec kafka");
//    }
//
//}
