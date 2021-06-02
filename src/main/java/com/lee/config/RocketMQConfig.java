package com.lee.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author lhj
 * @Date 2021/6/1 14:36
 */
@Configuration
@Slf4j
public class RocketMQConfig {

    @Bean(initMethod = "start",destroyMethod = "shutdown")
    public DefaultMQProducer mqProducer(){
        DefaultMQProducer mqProducer = new
                DefaultMQProducer(MQConfig.producerGroup);
        mqProducer.setNamesrvAddr(MQConfig.NAME_SERVER);
        return mqProducer;
    }


    @Bean(initMethod = "start",destroyMethod = "shutdown")
    public DefaultMQPushConsumer mqPushConsumer() throws MQClientException {
        DefaultMQPushConsumer mqConsumer = new
                DefaultMQPushConsumer(MQConfig.consumerGroup);
        mqConsumer.setNamesrvAddr(MQConfig.NAME_SERVER);
        mqConsumer.setMessageModel(MessageModel.CLUSTERING);
        mqConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        mqConsumer.subscribe(MQConfig.TOPIC,"");
        mqConsumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                MessageExt messageExt = list.get(0);
                log.info("消息id:{},消息内容:{},当前线程:{}",messageExt.getMsgId(),new String(messageExt.getBody()),Thread.currentThread().getName());
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
//        mqConsumer.registerMessageListener(new MessageListenerConcurrently() {
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeOrderlyContext) {
//                MessageExt messageExt = list.get(0);
//                log.info("消息id:{},消息内容:{},当前线程:{}",messageExt.getMsgId(),new String(messageExt.getBody()),Thread.currentThread().getName());
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });
        return mqConsumer;
    }
}
