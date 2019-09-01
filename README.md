# Money Transfer Application

RESTful API for money transfers between accounts. Implementation based on Spark Framework and HashMaps as in-memory DataSources.
Implemented two controllers to create/read/update account data and make transfer. Initial data is preloaded from JSON file. Http-Server starts on port 8182.

## Technologies
### Application
- Java 8
- Maven 3
- Lombok
- Jackson JSON
- Slf4j/Logback
- Spark Framework (http://sparkjava.com)
### Test
- JUnit
- Rest Assured
- Test NG
- JaCoCo
### Code validation
- Checkstyle (configuration "customized_sun_checks.xml" was extracted from IDEA Checkstyle plugin)
- PMD

## Building and running application
Build:
```
mvn clean package
```
Start:
```
java -jar target\MoneyTransfer-jar-with-dependencies.jar
```
Get account:
```
curl http://localhost:8182/moneytransfer/accounts/:id
```
Execute transaction:
```
curl -d "{\"amount\":BigDecimal,\"accountIdFrom\":\"String\",\"accountIdTo\":\"String\"}" -H "Content-Type: application/json" -X POST http://localhost:8182/moneytransfer/transfers
```
Example:
```
$ curl http://localhost:8182/moneytransfer/accounts/1
{"id":"1","balance":10}

$ curl http://localhost:8182/moneytransfer/accounts/2
{"id":"2","balance":10}

$ curl -d "{\"amount\":3,\"accountIdFrom\":\"1\",\"accountIdTo\":\"2\"}" -H "Content-Type: application/json" -X POST http://localhost:8182/moneytransfer/transfers
{"error":null,"success":true}

$ curl http://localhost:8182/moneytransfer/accounts/1
{"id":"1","balance":7}

$ curl http://localhost:8182/moneytransfer/accounts/2
{"id":"2","balance":13}
```

## TODO:
- Test coverage.
- Junit test for Controllers/Services/DAO.
- Add condition lock.

## Notes
Branch feature-read-write-lock contains implementation with ReadWriteLock but maven build had failed once.
