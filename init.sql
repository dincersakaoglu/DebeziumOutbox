-- Create a PostgreSQL database named "outbox_poc"
CREATE DATABASE debezium_outbox;

-- Connect to the created database
\c debezium_outbox;

-- Create a table named "accounts"
CREATE TABLE "public"."accounts"
(
    "id"           uuid NOT NULL,
    "username"     character varying(255),
    "mail"         character varying(255),
    "password"     character varying(255),
    "mailstatus"   character varying(255),
    "created_date" timestamp,
    CONSTRAINT "accounts_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

-- Create a table named "outboxs"
CREATE TABLE "public"."outboxs"
(
    "id"            uuid NOT NULL,
    "type"          character varying(255),
    "payload"       character varying(2000),
    CONSTRAINT "outboxs_pkey" PRIMARY KEY ("id")
) WITH (oids = false);
