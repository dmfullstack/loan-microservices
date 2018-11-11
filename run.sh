#!/bin/bash

set -e

cd `dirname $0`
r=`pwd`
echo $r

cd $r/eureka-server
echo "Starting Eureka Server..."
./mvnw -q clean spring-boot:run &

echo "Starting Loan Repository..."
cd $r/loan-repository
./mvnw -q clean spring-boot:run &

echo "Starting Loan Service..."
cd $r/loan-service
./mvnw -q clean spring-boot:run &