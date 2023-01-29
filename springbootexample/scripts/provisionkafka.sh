docker network create -d bridge springboot
docker run -d --name zookeeper --network springboot -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:latest
docker run -d --network springboot --name kafka -e ALLOW_PLAINTEXT_LISTENER=yes -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181 bitnami/kafka:latest
docker run --name activemq --network springboot -p 61616:61616 -p 8161:8161 -d rmohr/activemq
docker run --network springboot --name springboot -p 1090:1090 -v "C:/DEVELOPMENT:/DEVELOPMENT" -d arunsrajan/springbootcamel