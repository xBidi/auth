# spring
Project template for java spring

application in production
````
https://springsecurity.herokuapp.com/swagger-ui.html
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

deploy on heroku
````
heroku login
heroku create
heroku addons:create heroku-postgresql:hobby-dev
git push heroku master
heroku ps:scale web=1
heroku open
heroku logs --tail
````
