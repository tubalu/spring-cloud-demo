# spring-cloud-demo
# Folder Structure
## ./config 
config file
## ./config-service
provide central config for other modules
## ./eureka-service 
provide eureka service registration & discovery
## ./reservation-service
provide restful api service, when it start it will register it's self to eureka
## ./reservation-client
will talk to eureka, to find the reservation service .

# SETUP
## 1. git config repo
goto folder "config", run command:
```bash
git init
git add .
git commit -m "initial commit"
```
this will create local git repo for config file
## 2. run config service
in folder config-service
```bash
#cd config-service
mvn spring-boot:run
```
you can check applications properties by below links:
```bash
http://localhost:8888/reservation-service/master
http://localhost:8888/reservation-client/master
http://localhost:8888/eureka-service/master
```
## 3. start eureka

```bash
#cd eureka-service

mvn spring-boot:run
```
you can check goto, below address to see the registered services:
```
http://localhost:8761
```
## 4. start reservation service:
```bash
#cd reservation-service
mvn spring-boot:run
```
this service provide api via "spring rest reposity", sample links:
```bash
http://localhost:8000/reservations  #all reservations
http://localhost:8000/reservations/1 #single reservation
http://192.168.103.117:8000/reservations/search/by-name?rn=Scott #search by name
```

## 5. start reservation client:
```bash
#cd reservation-client
mvn spring-boot:run
```
check result by :
```bash
http://localhost:9999/reservations/names
```

