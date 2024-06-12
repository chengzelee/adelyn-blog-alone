FROM openjdk:21

ADD adelyn-blog-manager.jar adelyn-blog-manager.jar

RUN ln -sf /usr/share/zoneinfo/Asia/ShangHai /etc/localtime

CMD ["java","-jar","adelyn-blog-manager.jar"]
