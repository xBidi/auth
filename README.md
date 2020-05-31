# spring
Plantilla para proyectos de Spring

Aplicación desplegada
````
https://xbidi-spring.herokuapp.com/swagger-ui.html
````
Code formatter
````
https://raw.githubusercontent.com/google/styleguide/gh-pages/eclipse-java-google-style.xml
````
Gestor de dependencias
````
maven
````
Instalar java ubuntu
````
apt-get install openjdk-11-jdk
````

Lanzar aplicación en docker
````
sh deploy.sh
````

Lanzar aplicación en heroku
````
heroku login
heroku create
heroku config:set GOOGLE_CLIENT_ID=<client_id>
heroku config:set MONGODB_URI=<mongodb_uri>
heroku config:set SMTP_USERNAME=<smtp_password>
heroku config:set SMTP_PASSWORD=<smtp_username>
git push heroku master
heroku ps:scale web=1
heroku open
heroku logs --tail
````
