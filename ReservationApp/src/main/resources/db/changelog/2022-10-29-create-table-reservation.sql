CREATE TABLE IF NOT EXISTS reservation
(
    id              SERIAL      PRIMARY KEY,
    reservation_uid uuid UNIQUE NOT NULL,
    username        VARCHAR(80) NOT NULL,
    payment_uid     uuid        NOT NULL,
    hotel_id        INT REFERENCES hotels (id),
    status          VARCHAR(20) NOT NULL
        CHECK (status IN ('PAID', 'CANCELED')),
    start_date      TIMESTAMP WITH TIME ZONE,
    end_date        TIMESTAMP WITH TIME ZONE
);