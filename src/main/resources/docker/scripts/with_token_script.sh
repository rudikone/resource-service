#!/bin/bash

response=$(curl --location 'http://localhost:9000/api/auth/login' \
        --header 'Content-Type: application/json' \
        --data '{
          "login": "user",
          "password": "user"
        }')

token=$(echo $response | grep -oP '(?<="accessToken":")[^"]+')  # Извлекаем значение accessToken из ответа

while true; do
    num_requests=$(( ( RANDOM % 10 ) + 1 ))  # Генерируем случайное число от 1 до 10
    for ((i=1; i<=$num_requests; i++)); do
        curl --location 'http://localhost:9000/users' \
        --header "Authorization: Bearer $token"  # Выполняем новый запрос с полученным токеном
    done
    sleep 10
done