#!/bin/bash

while true; do
    num_requests=$(( ( RANDOM % 10 ) + 1 ))  # Generate a random number between 1 and 10
    for ((i=1; i<=$num_requests; i++)); do
        curl --location 'http://localhost:9000/api/auth/login' \
        --header 'Content-Type: application/json' \
        --data '{
          "login": "invlid",
          "password": "invalid"
        }'
    done
    sleep 10
done