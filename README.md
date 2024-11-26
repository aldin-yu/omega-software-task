# API Endpoints Documentation

### 1. Register User (POST /v1/auth/signup)
This `curl` command sends a POST request to the `/signup` endpoint to register a new user.

```bash
curl -X POST "http://localhost:8080/v1/auth/signup" \
     -H "Content-Type: application/json" \
     -d '{
           "email": "john.doe@example.com",
           "password": "password123",
           "fullName": "John Doe"
         }'
```

---

### 2. Login User (POST /v1/auth/login)
This `curl` command sends a POST request to the `/login` endpoint to authenticate a user and receive a JWT token.

```bash
curl -X POST "http://localhost:8080/v1/auth/login" \
     -H "Content-Type: application/json" \
     -d '{
           "email": "john.doe@example.com",
           "password": "password123"
         }'
```

---

### 3. Create Contract (POST /v1/api/contracts/create)
This `curl` command sends a POST request to the `/create` endpoint to create a new contract.

```bash
curl -X POST "http://localhost:8080/v1/api/contracts/create" \
     -H "Content-Type: application/json" \
     -d '{
           "buyer": "John Doe",
           "advancePaymentDate": "2024-06-01",
           "deliveryDeadline": "2024-07-01",
           "items": [
             {
               "name": "Item 1",
               "supplier": "Supplier A",
               "quantity": 10
             },
             {
               "name": "Item 2",
               "supplier": "Supplier B",
               "quantity": 5
             }
           ]
         }'
```

---

### 4. Fetch All Contracts (GET /v1/api/contracts/all)
This `curl` command sends a GET request to the `/all` endpoint to fetch all contracts filtered by buyer and status.

```bash
curl -X GET "http://localhost:8080/v1/api/contracts/all?buyer=John%20Doe&status=CREATED"
```

---

### 5. Fetch Contract (GET /v1/api/contracts/fetch)
This `curl` command sends a GET request to the `/fetch` endpoint to fetch a contract by its contract number.

```bash
curl -X GET "http://localhost:8080/v1/api/contracts/fetch?contractNumber=1/2024"
```

---

### 6. Update Contract (PATCH /v1/api/contracts/update)
This `curl` command sends a PATCH request to the `/update` endpoint to update an existing contract.

```bash
curl -X PATCH "http://localhost:8080/v1/api/contracts/update?contractNumber=1/2024" \
     -H "Content-Type: application/json" \
     -d '{
           "buyer": "Updated Buyer",
           "advancePaymentDate": "2024-07-01",
           "deliveryDeadline": "2024-08-01",
           "status": "DELIVERED"
         }'
```

---

### 7. Soft Delete Contract (DELETE /v1/api/contracts/delete)
This `curl` command sends a DELETE request to the `/delete` endpoint to delete a contract by its contract number.

```bash
curl -X DELETE "http://localhost:8080/v1/api/contracts/delete?contractNumber=1/2024"
```

---

### 8. Fetch Items (GET /v1/api/items/fetch)
This `curl` command sends a GET request to the `/fetch` endpoint to retrieve items by a contract number.

```bash
curl -X GET "http://localhost:8080/v1/api/items/fetch?contractNumber=1/2024"
```

---

### 9. Update Items (PATCH /v1/api/items/update)
This `curl` command sends a PATCH request to the `/update` endpoint to update items of a contract.

```bash
curl -X PATCH "http://localhost:8080/v1/api/items/update?contractNumber=1/2024" \
     -H "Content-Type: application/json" \
     -d '[
           {
             "id": 101,
             "name": "Updated Item Name",
             "supplier": "Updated Supplier",
             "quantity": 15,
             "status": "ORDERED"
           },
           {
             "id": 102,
             "name": "Another Updated Item",
             "supplier": "Another Supplier",
             "quantity": 20,
             "status": "ORDERED"
           }
         ]'
```

---

### 10. Soft Delete Items (DELETE /v1/api/items/delete)
This `curl` command sends a DELETE request to the `/delete` endpoint to soft delete items by their IDs for a specific contract number.

```bash
curl -X DELETE "http://localhost:8080/v1/api/items/delete?contractNumber=1/2024" \
     -H "Content-Type: application/json" \
     -d '[1, 2]'
```

