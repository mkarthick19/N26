#!/bin/bash
for i in {1..1}
do
 	curl -d "@transaction1.json" -H "Content-Type: application/json" -X POST http://localhost:8080/transactions  
done
