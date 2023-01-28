docker run -d --name zookeeper --network springboot -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:latest
docker run -d --network springboot --name kafka -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181 bitnami/kafka:latest
docker run -p 61616:61616 -p 8161:8161 rmohr/activemq