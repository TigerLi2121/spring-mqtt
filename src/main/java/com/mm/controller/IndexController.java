package com.mm.controller;

import com.mm.mqtt.MqttSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private MqttSender mqttSender;

    @GetMapping("/send/{topic}")
    public String hello(@PathVariable("topic") String topic, String msg) {
        mqttSender.sendToMqtt(topic, msg);
        return msg;
    }
}
