-- postgres db schema
SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET check_function_bodies = FALSE;
SET client_min_messages = WARNING;

CREATE SCHEMA IF NOT EXISTS customer_loyalty;
-- Enums
CREATE TYPE TransactionType AS ENUM ('ADD', 'SUBTRACT');
CREATE TYPE LoyaltyType AS ENUM ('ORDER', 'ORDER_CANCELLED', 'MANUAL_ENTRY');

CREATE CAST (character varying as TransactionType) WITH INOUT AS IMPLICIT;
CREATE CAST (character varying as LoyaltyType) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS customer_loyalty.points_history
(
    id                  UUID NOT NULL,
    points_id           UUID NOT NULL,
    customer_id         UUID NOT NULL,
    points              INTEGER NOT NULL,
    transaction_type    TransactionType NOT NULL,
    loyalty_type        LoyaltyType NOT NULL,
    reason              TEXT,
    created_at          TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS customer_loyalty.customer_points
(
    id               UUID NOT NULL,
    customer_id      UUID NOT NULL,
    total_points     INTEGER NOT NULL,
    created_at       TIMESTAMP WITH TIME ZONE,
    updated_at       TIMESTAMP WITH TIME ZONE
);

ALTER TABLE ONLY customer_loyalty.points_history ADD CONSTRAINT points_history_pkey PRIMARY KEY (id);
ALTER TABLE ONLY customer_loyalty.customer_points ADD CONSTRAINT points_pkey PRIMARY KEY (id);
CREATE UNIQUE INDEX points_customer_id_unique_index ON customer_loyalty.customer_points (customer_id);
ALTER TABLE ONLY customer_loyalty.points_history ADD CONSTRAINT points_history_points_id_fk FOREIGN KEY (points_id) REFERENCES customer_loyalty.customer_points(id);
