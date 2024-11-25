CREATE TYPE Contract AS (
    buyer TEXT,
    contract_number CHAR(10),
    down_payment_date DATE,
    delivery_date DATE,
    status TEXT,
    items item[],
    deleted BOOLEAN
);
