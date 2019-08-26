FROM openjdk:11
ADD target/find-10x-developer.jar /app.jar
ADD src/test/resources/input.txt /input.txt
ENTRYPOINT [ "java", "-jar", "/app.jar" ]