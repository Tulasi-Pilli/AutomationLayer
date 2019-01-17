package com.github.ptn006.v3;


import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class AvroConsumerV3 {

    public static void main(String[] args) {

        Properties properties = new Properties();

        // normal consumer
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        //different for consumer
        properties.setProperty("group.id", "Avro-consumer-V3");
        properties.setProperty("enable.auto.commit", "false");
        properties.setProperty("auto.offset.reset", "earliest");

        // avro part
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");
        properties.setProperty("specific.avro.reader", "true");

       KafkaConsumer<String, App_V3> kafkaConsumer = new KafkaConsumer<String, App_V3>(properties);
        String topic = "Initiate_Service"; //remains same

        kafkaConsumer.subscribe(Collections.singleton(topic)); //means subscribe to one topic

        System.out.println("Waiting for data..");

        while(true){
            ConsumerRecords<String, App_V3> consumerRecords = kafkaConsumer.poll(500);
            for(ConsumerRecord<String, App_V3> record : consumerRecords){
                App_V3 svc3 = record.value();
                System.out.println(svc3);

                if(svc3.getResultFlg().equals("Y"))
                    System.out.println("Results are available");

                else
                    System.out.println("Scan needs to be initiated to get the results");
            }

            kafkaConsumer.commitSync();
        }

        //kafkaConsumer.close(); not reachable and not required bcz of while loop is true

    }
}

// If consumer is restarted, we will see 'Waiting for data' as old records will be deleted.
// Again if we run Producer then we will see new reocrds.

