package com.daas.securityxref.util;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import com.google.pubsub.v1.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SubscriberUtility {

    public static void subscribeSync(
            String projectId, String subscriptionId, Integer numOfMessages) throws IOException {

        List<ReceivedMessage> recMsgList = new ArrayList<>();

        SubscriberStubSettings subscriberStubSettings =
                SubscriberStubSettings.newBuilder()
                        .setTransportChannelProvider(
                                SubscriberStubSettings.defaultGrpcTransportProviderBuilder()
                                        .setMaxInboundMessageSize(20 * 1024 * 1024) // 20MB (maximum message size).
                                        .build())
                        .build();

        try (SubscriberStub subscriber = GrpcSubscriberStub.create(subscriberStubSettings)) {
            String subscriptionName = ProjectSubscriptionName.format(projectId, subscriptionId);
            PullRequest pullRequest =
                    PullRequest.newBuilder()
                            .setMaxMessages(numOfMessages)
                            .setSubscription(subscriptionName)
                            .build();

            // Use pullCallable().futureCall to asynchronously perform this operation.
            PullResponse pullResponse = subscriber.pullCallable().call(pullRequest);
            List<String> ackIds = new ArrayList<>();
            for (ReceivedMessage message : pullResponse.getReceivedMessagesList()) {
                // Handle received message
                // ...
                recMsgList.add(message);
                ackIds.add(message.getAckId());
            }
            // Acknowledge received messages.
            AcknowledgeRequest acknowledgeRequest =
                    AcknowledgeRequest.newBuilder()
                            .setSubscription(subscriptionName)
                            .addAllAckIds(ackIds)
                            .build();

            // Use acknowledgeCallable().futureCall to asynchronously perform this operation.
            subscriber.acknowledgeCallable().call(acknowledgeRequest);
            System.out.println(pullResponse.getReceivedMessagesList());
        }
    }


    public static void subscribeAsync (String projectId, String subscriptionId) {

        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);

        // Instantiate an asynchronous message receiver.
        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    // Handle incoming message, then ack the received message.
                    System.out.println("Id: " + message.getMessageId());
                    System.out.println("Data: " + message.getData().toStringUtf8());
                    consumer.ack();
                };

        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            // Start the subscriber.
            subscriber.startAsync().awaitRunning();
            System.out.printf("Listening for messages on %s:\n", subscriptionName.toString());
            // Allow the subscriber to run for 30s unless an unrecoverable error occurs.
            subscriber.awaitTerminated(30, TimeUnit.SECONDS);
        } catch (TimeoutException timeoutException) {
            // Shut down the subscriber after 30s. Stop receiving messages.
            subscriber.stopAsync();
        }
    }
}


