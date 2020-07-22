package com.mm.controller;

import com.mm.mqtt.MqttSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private MqttSender mqttSender;

    @GetMapping("/send/{topic}")
    @ResponseBody
    public String send(@PathVariable("topic") String topic, String msg) {
        mqttSender.sendToMqtt(topic, msg);
        return msg;
    }

    @GetMapping("/hello")
    public String hello(String msg, Model model) {
        model.addAttribute("msg", msg);
        return "index";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
