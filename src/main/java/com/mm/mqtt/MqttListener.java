package com.mm.mqtt;

import com.mm.utils.ThrowableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.stereotype.Component;

@Component
public class MqttListener implements ApplicationListener<MqttConnectionFailedEvent> {

    public final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(MqttConnectionFailedEvent event) {
        log.error("MqttConnectionFailedEvent cause:", event.getCause());
        System.out.println("异常信息：" + ThrowableUtils.getStackTrace(event.getCause().fillInStackTrace()));
    }
}
