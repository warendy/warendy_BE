### 에러 내용(해결 완료)
kafka producer에서 send할 때는 serialize가 문제없이 작동하는 것 같은데
consumer에서 deserialize할 때, 에러가 생기는 것으로 추정됩니다.

### 에러 전문
2023-08-03T22:41:09.163+09:00 ERROR 35348 --- [anNameSet-0-C-1] o.s.k.l.KafkaMessageListenerContainer    : Consumer exception

java.lang.IllegalStateException: This error handler cannot process 'SerializationException's directly; please consider configuring an 'ErrorHandlingDeserializer' in the value and/or key deserializer
at org.springframework.kafka.listener.DefaultErrorHandler.handleOtherException(DefaultErrorHandler.java:198) ~[spring-kafka-3.0.1.jar:3.0.1]
at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.handleConsumerException(KafkaMessageListenerContainer.java:1955) ~[spring-kafka-3.0.1.jar:3.0.1]
at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:1390) ~[spring-kafka-3.0.1.jar:3.0.1]
at java.base/java.util.concurrent.CompletableFuture$AsyncRun.run(CompletableFuture.java:1804) ~[na:na]
at java.base/java.lang.Thread.run(Thread.java:833) ~[na:na]
Caused by: org.apache.kafka.common.errors.SerializationException: Error deserializing key/value for partition d95e0d91-2128-40b2-96f5-f2bc8d458743-0 at offset 0. If needed, please seek past the record to continue consumption.
Caused by: org.apache.kafka.common.errors.SerializationException: Can't deserialize data [[123, 34, 105, 100, 34, 58, 49, 44, 34, 115, 101, 110, 100, 101, 114, 34, 58, 34, 116, 101, 115, 116, 49, 50, 51, 64, 103, 109, 97, 105, 108, 46, 99, 111, 109, 34, 44, 34, 99, 111, 110, 116, 101, 110, 116, 34, 58, 34, 116, 101, 115, 116, 49, 50, 51, 64, 103, 109, 97, 105, 108, 46, 99, 111, 109, -21, -117, -104, -20, -99, -76, 32, -20, -98, -123, -20, -98, -91, -19, -107, -104, -20, -104, -128, -20, -118, -75, -21, -117, -120, -21, -117, -92, 46, 34, 44, 34, 116, 105, 109, 101, 115, 116, 97, 109, 112, 34, 58, 34, 50, 48, 50, 51, 45, 48, 56, 45, 48, 51, 84, 50, 50, 58, 52, 49, 58, 48, 50, 46, 54, 57, 54, 49, 49, 56, 51, 48, 48, 34, 44, 34, 114, 111, 111, 109, 73, 100, 34, 58, 34, 100, 57, 53, 101, 48, 100, 57, 49, 45, 50, 49, 50, 56, 45, 52, 48, 98, 50, 45, 57, 54, 102, 53, 45, 102, 50, 98, 99, 56, 100, 52, 53, 56, 55, 52, 51, 34, 44, 34, 99, 104, 97, 116, 82, 111, 111, 109, 34, 58, 123, 34, 114, 111, 111, 109, 73, 100, 34, 58, 34, 100, 57, 53, 101, 48, 100, 57, 49, 45, 50, 49, 50, 56, 45, 52, 48, 98, 50, 45, 57, 54, 102, 53, 45, 102, 50, 98, 99, 56, 100, 52, 53, 56, 55, 52, 51, 34, 44, 34, 99, 114, 101, 97, 116, 111, 114, 34, 58, 123, 34, 99, 114, 101, 97, 116, 101, 100, 65, 116, 34, 58, 34, 50, 48, 50, 51, 47, 48, 56, 47, 48, 51, 32, 50, 50, 58, 52, 48, 58, 51, 53, 34, 44, 34, 109, 111, 100, 105, 102, 105, 101, 100, 65, 116, 34, 58, 34, 50, 48, 50, 51, 47, 48, 56, 47, 48, 51, 32, 50, 50, 58, 52, 48, 58, 51, 53, 34, 44, 34, 100, 101, 108, 101, 116, 101, 100, 65, 116, 34, 58, 110, 117, 108, 108, 44, 34, 105, 100, 34, 58, 49, 44, 34, 101, 109, 97, 105, 108, 34, 58, 34, 116, 101, 115, 116, 49, 50, 51, 64, 103, 109, 97, 105, 108, 46, 99, 111, 109, 34, 44, 34, 112, 97, 115, 115, 119, 111, 114, 100, 34, 58, 34, 36, 50, 97, 36, 49, 48, 36, 53, 79, 87, 77, 109, 121, 89, 55, 76, 87, 110, 81, 105, 50, 115, 54, 67, 115, 49, 108, 108, 46, 69, 111, 47, 85, 115, 78, 86, 90, 78, 103, 76, 85, 66, 81, 115, 102, 72, 76, 117, 101, 71, 100, 107, 82, 73, 98, 81, 69, 87, 105, 50, 34, 44, 34, 110, 105, 99, 107, 110, 97, 109, 101, 34, 58, 34, -19, -103, -115, -22, -72, -72, -21, -113, -103, 34, 44, 34, 97, 118, 97, 116, 97, 114, 34, 58, 110, 117, 108, 108, 44, 34, 109, 98, 116, 105, 34, 58, 110, 117, 108, 108, 44, 34, 114, 111, 108, 101, 34, 58, 34, 77, 69, 77, 66, 69, 82, 34, 44, 34, 111, 97, 117, 116, 104, 84, 121, 112, 101, 34, 58, 110, 117, 108, 108, 44, 34, 98, 111, 100, 121, 34, 58, 49, 44, 34, 100, 114, 121, 34, 58, 49, 44, 34, 116, 97, 110, 110, 105, 110, 34, 58, 49, 44, 34, 97, 99, 105, 100, 105, 116, 121, 34, 58, 49, 44, 34, 97, 117, 116, 104, 111, 114, 105, 116, 105, 101, 115, 34, 58, 91, 123, 34, 97, 117, 116, 104, 111, 114, 105, 116, 121, 34, 58, 34, 82, 79, 76, 69, 95, 77, 69, 77, 66, 69, 82, 34, 125, 93, 125, 44, 34, 110, 97, 109, 101, 34, 58, 34, -20, -96, -107, -22, -78, -67, -20, -89, -124, -20, -99, -76, 32, -20, -103, -128, -20, -99, -72, -21, -89, -120, -20, -117, -92, 32, -20, -126, -84, -21, -98, -116, 32, -22, -75, -84, -19, -107, -76, -20, -102, -108, 33, 34, 44, 34, 109, 101, 109, 98, 101, 114, 78, 117, 109, 34, 58, 48, 125, 125]] from topic [d95e0d91-2128-40b2-96f5-f2bc8d458743]
Caused by: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `org.springframework.security.core.GrantedAuthority` (no Creators, like default constructor, exist): abstract types either need to be mapped to concrete types, have custom deserializer, or contain additional type information
at [Source: (byte[])"{"id":1,"sender":"test123@gmail.com","content":"test123@gmail.com님이 입장하였습니다.","timestamp":"2023-08-03T22:41:02.696118300","roomId":"d95e0d91-2128-40b2-96f5-f2bc8d458743","chatRoom":{"roomId":"d95e0d91-2128-40b2-96f5-f2bc8d458743","creator":{"createdAt":"2023/08/03 22:40:35","modifiedAt":"2023/08/03 22:40:35","deletedAt":null,"id":1,"email":"test123@gmail.com","password":"$2a$10$5OWMmyY7LWnQi2s6Cs1ll.Eo/UsNVZNgLUBQsfHLueGdkRIbQEWi2","nickname":"홍길동","avatar":null,"mbti":nu"[truncated 189 bytes]; line: 1, column: 592] (through reference chain: com.be.friendy.warendy.domain.chat.entity.Message["chatRoom"]->com.be.friendy.warendy.domain.chat.entity.ChatRoom["creator"]->com.be.friendy.warendy.domain.member.entity.Member["authorities"]->java.util.ArrayList[1])
at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:67) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.DeserializationContext.reportBadDefinition(DeserializationContext.java:1909) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:408) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.DeserializationContext.handleMissingInstantiator(DeserializationContext.java:1354) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.AbstractDeserializer.deserialize(AbstractDeserializer.java:274) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.std.CollectionDeserializer._deserializeFromArray(CollectionDeserializer.java:359) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.deserialize(CollectionDeserializer.java:272) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.deserialize(CollectionDeserializer.java:28) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.impl.SetterlessProperty.deserializeAndSet(SetterlessProperty.java:134) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserializeFromObject(BeanDeserializer.java:392) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserialize(BeanDeserializer.java:185) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.impl.MethodProperty.deserializeAndSet(MethodProperty.java:129) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserializeFromObject(BeanDeserializer.java:392) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserialize(BeanDeserializer.java:185) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.impl.MethodProperty.deserializeAndSet(MethodProperty.java:129) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserializeFromObject(BeanDeserializer.java:392) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserialize(BeanDeserializer.java:185) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.readRootValue(DefaultDeserializationContext.java:323) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.ObjectReader._bindAndClose(ObjectReader.java:2105) ~[jackson-databind-2.14.1.jar:2.14.1]
at com.fasterxml.jackson.databind.ObjectReader.readValue(ObjectReader.java:1583) ~[jackson-databind-2.14.1.jar:2.14.1]
at org.springframework.kafka.support.serializer.JsonDeserializer.deserialize(JsonDeserializer.java:585) ~[spring-kafka-3.0.1.jar:3.0.1]
at org.apache.kafka.clients.consumer.internals.Fetcher.parseRecord(Fetcher.java:1310) ~[kafka-clients-2.5.0.jar:na]
at org.apache.kafka.clients.consumer.internals.Fetcher.access$3500(Fetcher.java:128) ~[kafka-clients-2.5.0.jar:na]
at org.apache.kafka.clients.consumer.internals.Fetcher$CompletedFetch.fetchRecords(Fetcher.java:1541) ~[kafka-clients-2.5.0.jar:na]
at org.apache.kafka.clients.consumer.internals.Fetcher$CompletedFetch.access$1700(Fetcher.java:1377) ~[kafka-clients-2.5.0.jar:na]
at org.apache.kafka.clients.consumer.internals.Fetcher.fetchRecords(Fetcher.java:677) ~[kafka-clients-2.5.0.jar:na]
at org.apache.kafka.clients.consumer.internals.Fetcher.fetchedRecords(Fetcher.java:632) ~[kafka-clients-2.5.0.jar:na]
at org.apache.kafka.clients.consumer.KafkaConsumer.pollForFetches(KafkaConsumer.java:1290) ~[kafka-clients-2.5.0.jar:na]
at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1248) ~[kafka-clients-2.5.0.jar:na]
at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1216) ~[kafka-clients-2.5.0.jar:na]
at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollConsumer(KafkaMessageListenerContainer.java:1670) ~[spring-kafka-3.0.1.jar:3.0.1]
at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.doPoll(KafkaMessageListenerContainer.java:1645) ~[spring-kafka-3.0.1.jar:3.0.1]
at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.pollAndInvoke(KafkaMessageListenerContainer.java:1446) ~[spring-kafka-3.0.1.jar:3.0.1]
at org.springframework.kafka.listener.KafkaMessageListenerContainer$ListenerConsumer.run(KafkaMessageListenerContainer.java:1338) ~[spring-kafka-3.0.1.jar:3.0.1]
at java.base/java.util.concurrent.CompletableFuture$AsyncRun.run(CompletableFuture.java:1804) ~[na:na]
at java.base/java.lang.Thread.run(Thread.java:833) ~[na:na]

### 결과
로컬 테스트시 Message타입은 Entity지만 다른 테이블간의 연관관계를 설정하지 않았지만 프로젝트를 옮기는 과정에서
chat에서 만들었던 Entity 클래스들에 Member Entity와 연관관계를 설정하여 Deserialize 부분에서 에러가 생겼다.
새로운 dto 클래스 MessageDto 클래스를 만들어서 그걸로 kafka로 통신하고 DB에 저장할때만 Message로 바꿔서 저장하도록 진행했다.
