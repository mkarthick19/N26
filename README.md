N26 Coding Challenge:

We would like to have a restful API for our statistics. 
The main use case for our API is to calculate realtime statistic from the last 60 seconds. 
The API needs the following endpoints:

POST /transactions – called every time a transaction is made. It is also the sole input of this rest API.
GET /statistics – returns the statistic based of the transactions of the last 60 seconds.
DELETE /transactions – deletes all transactions.
 
Endpoints:

POST /transactions : For every new transaction, this endpoint is called.
GET /statistics : Returns the statistics based on the transactions that has happened in the last 60 seconds.
DELETE /statistics: To delete all transactions, this endpoint is called.



Design:

Our application initiates a array of fixed size [in our case, it is 60]. For each second, we aggregate all the valid transactions 
and store the item in that particular index. Each index in the array represents all the valid transactions that has 
happened in that particular second.

POST:

For every new transaction, once the input request is validated, we find the corresponding transaction index and create 
the new item (or) add to the existing transactions in that transaction index.

The time complexity is O(1), as it only put the valid transaction in the array (number of operations is constant)

GET :

In case of getting statistics, we aggregate all valid transactions indices and compute the statistics and returns it.

Time Complexity is O(1), as fetching all valid transaction is constant [60 indices at max] and computing statistics
is constant [60 indices at max]

DELETE:

This simply clears the array which is equivalent to deleting all the transactions.

Time complexity is O(1), as array is of fixed size [60 in our case].


Space complexity:

Space Complexity is O(1) for all the above operations, as we create an array is of fixed size [60 in our case].

Running the application :

mvn spring-boot:run

Build and run the tests :

mvn clean install

