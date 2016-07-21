#!/bin/sh

for file in ${COMMON_LIB_HOME}/**/*.jar;
do CP=${CP}:$file;
done

DUBBO_CONFIG_PATH=${COMMON_LIB_HOME}/config

CLASSPATH="${CP}"
CLASSPATH="${DUBBO_CONFIG_PATH}:${CLASSPATH}"
export CLASSPATH

MEM_ARGS="-Xms256m -Xmx512m -XX:PermSize=64M -XX:MaxPermSize=128M"
JAVA_OPTIONS="-Dfile.encoding=UTF-8 -Djava.net.preferIPv4Stack=true -Dsun.net.inetaddr.ttl=10 -Daid=${AID} -Dpaas.dubbo.provider.timeout=300000"

START_CMD="${MEM_ARGS} -Dpaas.dubbo.registry.address=${DUBBO_REGISTRY_ADDR} -Dpaas.dubbo.protocol.port=${DUBBO_PORT} ${JAVA_OPTIONS} com.ai.paas.ipaas.DubboServiceStart >> /ipaas-service-dubbo.log & 2 > 1 &"

echo ${JAVA_HOME}
echo ${CLASSPATH}
echo ${DUBBO_PORT}
echo ${START_CMD}

sed -i "s/jdbc.url=.*/jdbc.url=jdbc:mysql:\/\/${DB_HOST}\/${DB_NAME}?useUnicode=true\&characterEncoding=UTF-8/g" /iPaaS-Service-Dubbo/config/context/jdbc.properties
sed -i "s/jdbc.username=.*/jdbc.username=${DB_USRER}/g" /iPaaS-Service-Dubbo/config/context/jdbc.properties
sed -i "s/jdbc.password=.*/jdbc.password=${DB_PWD}/g" /iPaaS-Service-Dubbo/config/context/jdbc.properties

if [ -n "$LOG_LEVEL" ]; then  
    sed -i "s/<Root level=.*/<Root level=\"${LOG_LEVEL}\">/g" /iPaaS-Service-Dubbo/config/log4j2.xml
fi

echo "------------------- ipaas service dubbo start --------------------"
java ${START_CMD}
echo "------------------- ipaas service dubbo started! -------------------"
