# RSO: Blogs microservice

## Prerequisites

```bash
docker run -d --name pg-blogs -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=blogs -p 5432:5432 postgres:13
```

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar blogs-api-1.0.0-SNAPSHOT.jar
```
Available at: localhost:8080/v1/blogs

## Docker commands
```bash
docker build -t blogs .   
docker blogs
docker run blogs    
docker tag blogs mp6079/blogs   
docker push mp6079/blogs  
```

## Docker and environmental variables 
```bash
docker run --help
docker run -e MY_VAR=123
docker ps
docker build -t rso-dn
docker network ls
docker network create rso
docker network rm rso
docker rm -f pg-blogs
docker run -d --name pg-blogs -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=image-metadata -p 5432:5432 --network rso postgres:13
docker inspect pg-blogs
docker run -p 8080:8080 --network rso -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-blogs:5432/blogs rso-dn
```

## Consul
```bash
consul agent -dev
```
Available at: localhost:8500


## Kubernetes
```bash
kubectl version
kubectl --help
kubectl get nodes
kubectl create -f blogs-deployment.yaml 
kubectl apply -f blogs-deployment.yaml 
kubectl get services 
kubectl get deployments
kubectl get pods
kubectl logs blogs-deployment-6f59c5d96c-rjz46
kubectl delete pod blogs-deployment-6f59c5d96c-rjz46
```
