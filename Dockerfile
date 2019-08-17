#
# Quack TMS
#

# Pull base image.
FROM openjdk:8u212-slim-stretch

#Application
RUN mkdir -p /usr/liken
COPY assembly/target/liken*.jar /usr/liken
COPY assembly/target/lib /usr/liken/lib

#Configs
RUN mkdir -p /etc/liken
COPY assembly/liken.properties /etc/liken

#UI
RUN mkdir -p /usr/liken/ui
COPY ui/src/build/ /usr/liken/ui

# Install NGINX
RUN apt-get update
RUN apt-get -y install nginx

COPY assembly/liken.conf /etc/nginx/sites-available
RUN ln -s /etc/nginx/sites-available/liken.conf /etc/nginx/sites-enabled/liken.conf
RUN nginx

#Startup
RUN mkdir -p /usr/liken/bin
COPY assembly/liken.sh /usr/liken/bin

EXPOSE 80
ENTRYPOINT ["sh", "/usr/quack/bin/startup.sh"]

