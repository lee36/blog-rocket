package com.lee;

import com.lee.config.MQConfig;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Author lhj
 * @Date 2021/6/1 14:36
 */
@SpringBootApplication
@RestController
public class BlogRocketApplication{

    @Autowired
    private MQProducer mqProducer;

    public static void main(String[] args) {
        SpringApplication.run(BlogRocketApplication.class,args);
    }

    @RequestMapping("/test")
    public Object sendMsg(String msg) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message message = new Message(MQConfig.TOPIC,msg.getBytes());
        SendResult send = mqProducer.send(message);
        return send;
    }

    @RequestMapping("/test1")
    public Object sendMsg1() throws InterruptedException, RemotingException, MQClientException, MQBrokerException { List<Message> MessageList=initData();
        for (Message message : MessageList) {
            SendResult send = mqProducer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    int i = Integer.parseInt((String) o);
                    return list.get(i%list.size());
                }
            },message.getKeys());
        }

        return "OK";
    }


    @RequestMapping("/test2")
    public Object sendMsg2(String msg) throws InterruptedException, RemotingException, MQClientException, MQBrokerException { List<Message> MessageList=initData();
        Message message = new Message(MQConfig.TOPIC,msg.getBytes());
        message.setDelayTimeLevel(2);
        SendResult send = mqProducer.send(message);
        return send;
    }

    private List<Message> initData() {
        List<Integer> orderList=new ArrayList<>();
        IntStream.of(10231,3131,43212,909089,7878)
                .forEach(orderList::add);
        List<String> statusList=new ArrayList<>();
        Stream.of("创建订单","支付订单","完成订单")
                .forEach(statusList::add);
        List<Message> messages = new ArrayList<>();
        for (Integer integer : orderList) {
            for (String s : statusList) {
                messages.add(new Message(MQConfig.TOPIC,"good",integer+"",(integer+s).getBytes()));
            }
        }
        return messages;
    }
}
