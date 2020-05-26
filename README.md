# spring
Project template for java spring

application in production
````
https://springsecuritydev.herokuapp.com/swagger-ui.html
https://springsecuritypre.herokuapp.com
https://springsecuritypro.herokuapp.com
https://circleci.com/gh/xBidi/spring
https://sonarcloud.io/dashboard?id=xBidi_spring
````
code style
````
https://raw.githubusercontent.com/google/styleguide/gh-pages/eclipse-java-google-style.xml
````
package manager
````
maven
````
java jdk
````
openjdk-8
````

generate jar
````
mvn install
````

docker deploy
````
docker volume create db-data
docker-compose build
docker-compose up -d
````

docker stop
````
docker-compose down
````

Driver example for external apps
````
Driver driver = new Driver("http://diegotobalina97.ddns.net:8000/api/v1");
String login = driver.login("user", "user", "password");
System.out.println(login);
````
