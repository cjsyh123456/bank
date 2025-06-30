# build docker image
docker build -t bank-transaction-service .

# tag image
docker tag bank-transaction-service your-registry/bank-transaction-service:latest

# push image
docker push your-registry/bank-transaction-service:latest

# apply k8s configs
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/hpa.yaml

# check status
kubectl get pods -w
kubectl get services
kubectl get hpa