/* Setting up PROD DB */
create database prod;
\c prod;

CREATE SEQUENCE public.user_id_seq;

CREATE TABLE public.User (
                id INTEGER NOT NULL DEFAULT nextval('public.user_id_seq'),
                username VARCHAR(100) NOT NULL,
                phone_number VARCHAR(15) NOT NULL,
                email_address VARCHAR(100) NOT NULL,
                latest_location_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                CONSTRAINT user_pk PRIMARY KEY (id)
);


INSERT INTO public.user 
(username, phone_number, email_address) 
VALUES 
('adrien','0645454545','adrien@mail.fr'),
('Jeanne','0645050500','jeanne@mail.fr'),
('Nathalie','054545454','nathalie@mail.fr')
;
