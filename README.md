# How to run the application

```terminal
$ mvn package
$ mvn spring-boot:run
```

### Endpoints

- [`/currency`](localhost:8080/currency) - get current info about USD value.
- [`/currency?range=10`](localhost:8080/currency?range=10) - get last 10 days history of USD value.
- [`/weather`](localhost:8080/weather) - get current weather in Moscow.
- [`/weather?range=8`](localhost:8080/weather?range=8) - get last 8 days weather history. **Warning:** there is no 
guarantee that you are able to get info about any longer period.
- [`/weather?city=New-York`](localhost:8080/weather?city=New-York) - get current weather in New-York. You can combine `city` 
and `range` parameters.
- [`/predict`](localhost:8080/predict) - get prediction about tomorrow USD value depending on data of currency 
value and weather (average temperature) for the last 8 days.