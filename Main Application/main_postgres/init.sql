DROP DATABASE IF EXISTS TestDatabase;

CREATE DATABASE TestDatabase
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    TEMPLATE = template0;

\c TestDatabase;

CREATE TABLE IF NOT EXISTS public.forms
(
    form_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    owner_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    vehicle_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    date_submitted timestamp(6) without time zone NOT NULL,
    status character varying(255) COLLATE pg_catalog."default",
	tax_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT forms_pkey PRIMARY KEY (form_id),
    CONSTRAINT form_id_pk UNIQUE (form_id)
);

ALTER TABLE IF EXISTS public.forms
    OWNER to postgres;
	
CREATE TABLE IF NOT EXISTS public.vehicles
(
    vehicle_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    owner_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    make character varying(255) COLLATE pg_catalog."default" NOT NULL,
    model character varying(255) COLLATE pg_catalog."default" NOT NULL,
    year integer NOT NULL,
    displacement integer NOT NULL,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    color character varying(255) COLLATE pg_catalog."default",
    date_of_inspection timestamp(6) without time zone,
    CONSTRAINT vehicles_pkey PRIMARY KEY (vehicle_id),
    CONSTRAINT "PK_VEHICLE_ID" UNIQUE (vehicle_id)
);

ALTER TABLE IF EXISTS public.vehicles
    OWNER to postgres;