echo "Build and push LoyaltyApp"
docker build -t bryanskaya/loyaltyapp:latest ../LoyaltyApp/.
docker push bryanskaya/loyaltyapp

echo "Build and push PaymentApp"
docker build -t bryanskaya/paymentapp:latest ../PaymentApp/.
docker push bryanskaya/paymentapp

echo "Build and push ReservationApp"
docker build -t bryanskaya/reservationapp:latest ../ReservationApp/.
docker push bryanskaya/reservationapp

echo "Build and push Gateway"
docker build -t bryanskaya/gateway:latest ../Gateway/.
docker push bryanskaya/gateway