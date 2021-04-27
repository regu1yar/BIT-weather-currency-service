# How to run the application

```terminal
$ mvn package
$ docker-compose up
```

### Endpoints

- `localhost:8080/currency` ([link to localhost](http://localhost:8080/currency)) - get current info about USD value.
- `localhost:8080/currency?range=10` ([link to localhost](http://localhost:8080/currency?range=10)) - get last 10 days history of USD value.
- `localhost:8081/weather` ([link to localhost](http://localhost:8081/weather)) - get current weather in Moscow.
- `localhost:8081/weather?range=8` ([link to localhost](http://localhost:8081/weather?range=8)) - get last 8 days weather history. **Warning:** there is no 
guarantee that you are able to get info about any longer period.
- `localhost:8081/weather?city=New-York` ([link to localhost](http://localhost:8081/weather?city=New-York)) - get current weather in New-York. You can combine `city` 
and `range` parameters.
- `localhost:8082/predict` ([link to localhost](http://localhost:8082/predict)) - get prediction about tomorrow USD value depending on data of currency 
value and weather (average temperature) for the last 8 days.
- `localhost:8761` ([link to localhost](http://localhost:8761)) - Eureka dashboard