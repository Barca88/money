# Money Transaction Management API Documentation

## Overview

The Money Transaction Management System is a RESTful API built with Spring Boot that manages scheduled financial transactions between accounts. The system automatically calculates transaction fees based on the transfer amount and schedule date.

## Base URL

```
http://localhost:8080/api/transactions
```

## API Endpoints

### 1. Get All Transactions

Retrieves a list of all transactions in the system.

**Endpoint:** `GET /api/transactions`

**Response:**
- **Status Code:** 200 OK
- **Body:** Array of Transaction objects

**Example Request:**
```bash
GET http://localhost:8080/api/transactions
```

**Example Response:**
```json
[
  {
    "id": 1,
    "accountOrigin": "123456789",
    "accountDestination": "987654321",
    "creationDate": "2026-01-21",
    "scheduleDate": "2026-01-22",
    "amount": 500.00,
    "fee": 0
},
{
    "id": 2,
    "accountOrigin": "123456789",
    "accountDestination": "987654321",
    "creationDate": "2026-01-21",
    "scheduleDate": "2026-01-22",
    "amount": 1500.00,
    "fee": 135.0000
}
]
```

---

### 2. Get Transaction by ID

Retrieves a specific transaction by its unique identifier.

**Endpoint:** `GET /api/transactions/{id}`

**Path Parameters:**
- `id` (Long) - The unique identifier of the transaction

**Response:**
- **Status Code:** 200 OK - Transaction found
- **Status Code:** 404 Not Found - Transaction not found
- **Body:** Transaction object (if found)

**Example Request:**
```bash
GET http://localhost:8080/api/transactions/1
```

**Example Response:**
```json
{
    "id": 7,
    "accountOrigin": "123456789",
    "accountDestination": "987654321",
    "creationDate": "2026-01-21",
    "scheduleDate": "2026-01-22",
    "amount": 500.00,
    "fee": 0.00
}
```

---

### 3. Create Transaction

Creates a new scheduled transaction. The system automatically sets the creation date and calculates the transaction fee.

**Endpoint:** `POST /api/transactions`

**Request Body:** TransactionRequestDTO
```json
{
  "accountOrigin": "string (required)",
  "accountDestination": "string (required)",
  "scheduleDate": "date (required, format: YYYY-MM-DD)",
  "amount": "decimal (required)"
}
```

**Response:**
- **Status Code:** 200 OK
- **Body:** Created Transaction object with calculated fee

**Example Request:**
```bash
POST http://localhost:8080/api/transactions
Content-Type: application/json

{
  "accountOrigin": "123456789",
  "accountDestination": "987654321",
  "scheduleDate": "2026-01-21",
  "amount": 500.00
}
```

**Example Response:**
```json
{
  "id": 3,
  "accountOrigin": "123456789",
  "accountDestination": "987654321",
  "creationDate": "2026-01-21",
  "scheduleDate": "2026-01-21",
  "amount": 500.00,
  "fee": 18.00
}
```

---

### 4. Update Transaction

Updates an existing transaction. The fee is recalculated based on the new amount and schedule date.

**Endpoint:** `PUT /api/transactions/{id}`

**Path Parameters:**
- `id` (Long) - The unique identifier of the transaction to update

**Request Body:** TransactionRequestDTO
```json
{
  "accountOrigin": "string (required)",
  "accountDestination": "string (required)",
  "scheduleDate": "date (required, format: YYYY-MM-DD)",
  "amount": "decimal (required)"
}
```

**Response:**
- **Status Code:** 200 OK - Transaction updated successfully
- **Status Code:** 404 Not Found - Transaction not found
- **Body:** Updated Transaction object (if found)

**Example Request:**
```bash
PUT http://localhost:8080/api/transactions/1
Content-Type: application/json

{
  "accountOrigin": "123456789",
  "accountDestination": "987654321",
  "scheduleDate": "2026-02-15",
  "amount": 1500.00
}
```

**Example Response:**
```json
{
  "id": 1,
  "accountOrigin": "123456789",
  "accountDestination": "987654321",
  "creationDate": "2026-01-21",
  "scheduleDate": "2026-02-15",
  "amount": 1500.00,
  "fee": 135.00
}
```

---

### 5. Delete Transaction

Deletes a transaction by its unique identifier.

**Endpoint:** `DELETE /api/transactions/{id}`

**Path Parameters:**
- `id` (Long) - The unique identifier of the transaction to delete

**Response:**
- **Status Code:** 204 No Content

**Example Request:**
```bash
DELETE http://localhost:8080/api/transactions/1
```

---

## Transaction Fee Calculation

Transaction fees are automatically calculated based on the transfer amount and the number of days between the creation date and the scheduled date.

### Fee Rules

| Amount Range | Days Until Scheduled Date | Fee Calculation |
|--------------|---------------------------|-----------------|
| ≤ $1,000 | 0 (same day) | 3% + $3 flat fee |
| ≤ $2,000 | 1-10 days | 9% |
| > $2,000 | 11-20 days | 8.2% |
| > $2,000 | 21-30 days | 6.9% |
| > $2,000 | 31-40 days | 4.7% |
| > $2,000 | 40+ days | 1.7% |
| All others | - | $0 |

### Fee Calculation Examples

1. **Same Day Transfer - Small Amount:**
   - Amount: $500
   - Schedule: Same day (0 days)
   - Fee: $500 × 0.03 + $3 = $18.00

2. **Short-term Transfer:**
   - Amount: $1,500
   - Schedule: 5 days from now
   - Fee: $1,500 × 0.09 = $135.00

3. **Long-term Transfer - Large Amount:**
   - Amount: $5,000
   - Schedule: 45 days from now
   - Fee: $5,000 × 0.017 = $85.00

---

## Data Models

### Transaction

The main entity representing a financial transaction.

```json
{
  "id": "Long - Unique identifier (auto-generated)",
  "accountOrigin": "String - Origin account number (required)",
  "accountDestination": "String - Destination account number (required)",
  "creationDate": "LocalDate - Date when created (auto-set)",
  "scheduleDate": "LocalDate - Scheduled execution date (required)",
  "amount": "BigDecimal - Transfer amount (required)",
  "fee": "BigDecimal - Calculated transaction fee (auto-calculated)"
}
```

### TransactionRequestDTO

Data transfer object used for creating or updating transactions.

```json
{
  "accountOrigin": "String - Origin account number (required)",
  "accountDestination": "String - Destination account number (required)",
  "scheduleDate": "LocalDate - Scheduled execution date (required)",
  "amount": "BigDecimal - Transfer amount (required)"
}
```

---

## Error Responses

### 400 Bad Request
Returned when validation fails on the request body.

```json
{
  "timestamp": "2026-01-21T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed"
}
```

### 404 Not Found
Returned when a transaction with the specified ID is not found.

```json
{
  "timestamp": "2026-01-21T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Transaction not found"
}
```

---

## Testing the API

### Using Bruno

The project includes Bruno API collection files in the `Bruno/Money/` directory:

- `Create Transaction.bru` - Create a new transaction
- `Get All Transactions.bru` - Retrieve all transactions
- `Get by Id.bru` - Retrieve a specific transaction
- `Update a scheduled transaction.bru` - Update a transaction
- `Delete a scheduled transaction.bru` - Delete a transaction

### Using cURL

**Create a transaction:**
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "accountOrigin": "123456789",
    "accountDestination": "987654321",
    "scheduleDate": "2026-01-25",
    "amount": 1000.00
  }'
```

**Get all transactions:**
```bash
curl http://localhost:8080/api/transactions
```

**Get transaction by ID:**
```bash
curl http://localhost:8080/api/transactions/1
```

**Update a transaction:**
```bash
curl -X PUT http://localhost:8080/api/transactions/1 \
  -H "Content-Type: application/json" \
  -d '{
    "accountOrigin": "123456789",
    "accountDestination": "987654321",
    "scheduleDate": "2026-02-01",
    "amount": 1500.00
  }'
```

**Delete a transaction:**
```bash
curl -X DELETE http://localhost:8080/api/transactions/1
```

---

## Validation Rules

All required fields must be provided in create and update requests:

- `accountOrigin`: Must not be null
- `accountDestination`: Must not be null
- `scheduleDate`: Must not be null, format: YYYY-MM-DD
- `amount`: Must not be null, must be a valid decimal number

---

## Notes

- All monetary amounts are handled using `BigDecimal` to ensure precision
- Dates use the ISO-8601 format (YYYY-MM-DD)
- The creation date is automatically set to the current date when a transaction is created
- Transaction fees are recalculated whenever a transaction is updated
- The fee calculation uses the current date (at the time of save/update) to determine days until scheduled date
