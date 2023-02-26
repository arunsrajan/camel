docker run -d --name zookeeper --network redis_redis-cluster -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:latest
docker run -d --network redis_redis-cluster --name kafka -e ALLOW_PLAINTEXT_LISTENER=yes -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181 bitnami/kafka:latest
docker run --name activemq --network redis_redis-cluster -p 61616:61616 -p 8161:8161 -d rmohr/activemq
docker run --network redis_redis-cluster --name springboot1 -p 1090:1090 -v "C:/DEVELOPMENT:/DEVELOPMENT" -d springbootcamel
docker run --network redis_redis-cluster --name springboot2 -p 1091:1090 -v "C:/DEVELOPMENT:/DEVELOPMENT" -d springbootcamel
docker exec -it redis-redis-1-1 redis-cli --cluster create redis-1:6379 redis-2:6379 redis-3:6379 redis-4:6379 redis-5:6379 redis-6:6379 --cluster-replicas 6 --cluster-yes