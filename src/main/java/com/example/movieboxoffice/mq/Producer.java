package com.example.movieboxoffice.mq;

import org.springframework.stereotype.Component;

@Component
public class Producer {

//    @Value("${rocketAddress}")
//    private String rocketAddress;
//
//    public void actorDetail(String message) throws MQClientException {
//        DefaultMQProducer producer = new DefaultMQProducer("actor");
//        producer.setNamesrvAddr(rocketAddress);
//        producer.start();
//        Message msg = new Message("actorDetail", message.getBytes(StandardCharsets.UTF_8));
//        try {
//            producer.send(msg);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        producer.shutdown();
//    }
}
