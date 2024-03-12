# Health check

```shell
curl --location 'http://localhost:5001/api'
```


```shell
curl --location 'http://localhost:5001/api/allAccounts'
```

# Send money
```shell
curl --location 'http://localhost:5001/api/sendMoney' \
--header 'Content-Type: application/json' \
--data '{
    "fromAccount":1001,
    "toAccount":3001,
    "amount":50.0
}'
```

# Receive Money
```shell
curl --location 'http://localhost:5001/api/receiveMoney' \
--header 'Content-Type: application/json' \
--data '{
    "fromAccount":1001,
    "toAccount":3001,
    "amount":50.0
}'
```