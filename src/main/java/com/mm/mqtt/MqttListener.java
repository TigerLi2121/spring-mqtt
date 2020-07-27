package com.mm.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.integration.mqtt.event.MqttSubscribedEvent;
import org.springframework.stereotype.Component;

@Component
public class MqttListener implements ApplicationListener {

    public final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof MqttConnectionFailedEvent) {
            MqttConnectionFailedEvent e = (MqttConnectionFailedEvent) event;
            log.error("MqttConnectionFailedEvent cause:", e.getCause());
        }
        if (event instanceof MqttSubscribedEvent) {
            MqttSubscribedEvent e = (MqttSubscribedEvent) event;
            log.info("MqttSubscribedEvent message:{}", e.toString());
        }
    }
}
