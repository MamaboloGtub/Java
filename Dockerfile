FROM amazoncorretto:21
WORKDIR /src/java
COPY . /src/java
# RUN java WeatherMainClass.java
CMD ["java", "WeatherMainClass.java"]