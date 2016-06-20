# Pull base image  
FROM centos:7

RUN yum install -y java-1.8.0-openjdk

# deploy user dubbo service
RUN mkdir iPaaS-Service-Dubbo && cd /iPaaS-Service-Dubbo && mkdir 3rd-libs lib config
COPY ./build/3rd-libs/*.jar /iPaaS-Service-Dubbo/3rd-libs/
COPY ./build/libs/*.jar /iPaaS-Service-Dubbo/lib/
COPY ./build/all-config /iPaaS-Service-Dubbo/config/

# remove some libs
RUN rm -rf /iPaaS-Service-Dubbo/3rd-libs/httpcore-4.2.5.jar 
RUN rm -rf /iPaaS-Service-Dubbo/3rd-libs/httpclient-4.2.6.jar

## copy config files
RUN cd /iPaaS-Service-Dubbo/config && mkdir context
COPY ./context/* /iPaaS-Service-Dubbo/config/context/

## copy start script
COPY ./ipaas_service_dubbo.sh /ipaas_service_dubbo.sh
RUN chmod 755 /ipaas_service_dubbo.sh

# set start parameter for dubbo service
ENV COMMON_LIB_HOME /iPaaS-Service-Dubbo
ENV PATH $CATALINA_HOME/bin:$PATH

ENV DUBBO_PORT="20998"
ENV DUBBO_SERVER_NAME="IPAAS-SERVICE-SERV"
ENV DUBBO_REGISTRY_ADD="10.1.228.199:49181,10.1.228.200:49181,10.1.228.202:49181"
ENV DUBBO_CONFIG_PATH=$COMMON_LIB_HOME/config
ENV PROCESS_PARM="paas.dubbo.protocol.port=$DUBBO_PORT"
ENV MEM_ARGS="-Xms256m -Xmx512m -XX:PermSize=64M -XX:MaxPermSize=128M"
ENV JAVA_OPTIONS="-Dfile.encoding=UTF-8 -Djava.net.preferIPv4Stack=true -Dsun.net.inetaddr.ttl=10 -Dpaas.dubbo.provider.timeout=300000"

# Expose ports.
EXPOSE 20998

# Define default command.
CMD ["/ipaas_service_dubbo.sh"]