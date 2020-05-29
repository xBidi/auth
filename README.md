# spring
Project template for java spring

deployed application
````
https://xbidi-spring.herokuapp.com/swagger-ui.html
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
docker-compose down
docker-compose up -d
docker-compose logs -f 2>&1 | ccze -m ansi
````
launch server on heroku
````
heroku login
heroku create
heroku config:set google_client_id=<client_id>
heroku config:set mongodb_uri=<mongodb_uri>
heroku config:set smtp_password=<smtp_password>
heroku config:set smtp_username=<smtp_username>
git push heroku master
heroku ps:scale web=1
heroku open
heroku logs --tail
````
