package com.daas.securityxref.util;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PublisherUtility {

    public static void publishMessage(String projectId,
                                        String topicId,
                                        String topicName,
                                        String messageBody) throws IOException, ExecutionException, InterruptedException {
        Publisher publisher = null;

        try {
            publisher = Publisher.newBuilder(topicName).build();
//            String message = "Hello World!";
            String message = messageBody;
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
            String messageId = (String)messageIdFuture.get();
            System.out.println("Published message ID: " + messageId);
        } catch (Exception var12) {
            System.out.println(var12);
        } finally {
            if (publisher != null) {
                publisher.shutdown();
                publisher.awaitTermination(1L, TimeUnit.MINUTES);
            }
        }
    }
}
