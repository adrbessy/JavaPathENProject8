/* Setting up PROD DB */
create database prod;
\c prod;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE public.User (
        id uuid DEFAULT uuid_generate_v4 (),
        user_name VARCHAR(100) NOT NULL UNIQUE,
        phone_number VARCHAR(15) NOT NULL,
        email_address VARCHAR(100) NOT NULL,
        latest_location_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        CONSTRAINT user_pk PRIMARY KEY (id)
);

INSERT INTO public.user 
(user_name, phone_number, email_address) 
VALUES 
('adrien','0645454545','adrien@mail.fr'),
('Jeanne','0645050500','jeanne@mail.fr'),
('Nathalie','054545454','nathalie@mail.fr')
;


CREATE SEQUENCE public.visited_location_id_seq;

CREATE TABLE public.Visited_location (
		id SMALLINT NOT NULL DEFAULT nextval('public.visited_location_id_seq'),
        user_id uuid NOT NULL,
        timeVisited TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        CONSTRAINT visited_location_pk PRIMARY KEY (id)
);

ALTER SEQUENCE public.visited_location_id_seq OWNED BY public.visited_location.id;


CREATE TABLE public.Location (
		visited_location_id SMALLINT NOT NULL,
        longitude NUMERIC(10,2) NOT NULL,
        latitude NUMERIC(10,2) NOT NULL
);

ALTER TABLE public.Visited_location ADD CONSTRAINT user_visited_location_fk
FOREIGN KEY (user_id)
REFERENCES public.User (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Location ADD CONSTRAINT user_location_fk
FOREIGN KEY (visited_location_id)
REFERENCES public.Visited_location (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

