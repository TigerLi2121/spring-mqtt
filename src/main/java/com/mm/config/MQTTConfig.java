package com.mm.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@EnableIntegration
@Configuration
public class MQTTConfig {

    public static final Logger log = LoggerFactory.getLogger(MQTTConfig.class);

    /**
     * 订阅的bean名称
     */
    public static final String CHANNEL_NAME_IN = "mqttInboundChannel";
    /**
     * 发布的bean名称
     */
    public static final String CHANNEL_NAME_OUT = "mqttOutboundChannel";

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{"tcp://195.168.168.150:1883"});
//        options.setUserName("guest");
//        options.setPassword("guest".toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * MQTT信息通道（生产者）
     */
    @Bean(name = CHANNEL_NAME_OUT)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（生产者）
     *
     * @return {@link org.springframework.messaging.MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                "mqttProducer",
                mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("topic1");
        return messageHandler;
    }

    /**
     * MQTT消息订阅绑定（消费者）
     */
    @Bean
    public MessageProducer inbound() {
        // 同时消费（订阅）所有Topic
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "mqttConsumer", mqttClientFactory(), "+/#");
//                "mqttConsumer2", mqttClientFactory(), "$share/g/aaa");  // 共享订阅 $share/g/是共享订阅前缀 真实主题名:aaa
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }

    /**
     * MQTT信息通道（消费者）
     */
    @Bean(name = CHANNEL_NAME_IN)
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（消费者）
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    public MessageHandler handler() {

        return message -> {
            String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);
            String payload = String.valueOf(message.getPayload());
            log.info("topic:{}; payload:{}", topic, payload);
        };
    }
}
