INSERT INTO PAYMENT_INFO (ID, ACCOUNT_NUMBER) VALUES ('f3b9a365-36c8-4990-8f1a-7f2e9dc0f400', '324324-2343242-23432');
INSERT INTO PAYMENT_INFO (ID, ACCOUNT_NUMBER) VALUES ('558eb804-bee1-44f2-92ef-a47606c7683d', '755534-2133445-00942');
INSERT INTO PAYMENT_INFO (ID, ACCOUNT_NUMBER) VALUES ('984d405d-4ead-43d7-8171-ae69d4a7c08f', '143256-9438483-20092');

INSERT INTO LOCATION (ID, NAME, LONGITUDE, LATITUDE) VALUES ('b74b7c57-4542-471f-8539-4559e9489997', 'FTN', 45.246, 19.8512);
INSERT INTO LOCATION (ID, NAME, LONGITUDE, LATITUDE) VALUES ('ed726dda-44c8-4953-9e21-8ce41ed4d370', 'Knjizara', 45.253672, 19.841270);
INSERT INTO LOCATION (ID, NAME, LONGITUDE, LATITUDE) VALUES ('e16c8018-c069-4aef-b315-a00327efff36', 'Porodiliste', 45.258208, 19.829756);

INSERT INTO VEHICLE (ID, LONGITUDE, LATITUDE, BABY_FRIENDLY, PET_FRIENDLY, CAPACITY) VALUES ('3d2becd5-aa6f-4410-9058-bed41a017457', 45.246, 19.8512, FALSE, TRUE, 4);
INSERT INTO VEHICLE (ID, LONGITUDE, LATITUDE, BABY_FRIENDLY, PET_FRIENDLY, CAPACITY) VALUES ('e63dbc27-1a36-4f73-b024-07e19ea91838', 45.246, 19.8512, FALSE, FALSE, 4);
INSERT INTO VEHICLE (ID, LONGITUDE, LATITUDE, BABY_FRIENDLY, PET_FRIENDLY, CAPACITY) VALUES ('6f513eca-b592-4e88-9cb4-ad9c14301ab2', 45.246, 19.8512, TRUE, TRUE, 7);

INSERT INTO DRIVER (MAIL, PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, ACTIVE, PHOTO_PATH, PAYMENT_INFO_ID, VEHICLE_ID)
VALUES ( 'zdravko.zdravkovic@gmail.com', 'sifra', 'Zdravko', 'Zdravkovic', '+38164548952', 'Novi Sad', TRUE, 'zdravko1.png', 'f3b9a365-36c8-4990-8f1a-7f2e9dc0f400', '3d2becd5-aa6f-4410-9058-bed41a017457');

INSERT INTO DRIVER (MAIL, PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, ACTIVE, PHOTO_PATH, PAYMENT_INFO_ID, VEHICLE_ID)
VALUES ( 'marko.markovic@gmail.com', 'sifra', 'Marko', 'Markovic', '+38164432452', 'Novi Sad', TRUE, 'marko1.png', '558eb804-bee1-44f2-92ef-a47606c7683d', 'e63dbc27-1a36-4f73-b024-07e19ea91838');

INSERT INTO DRIVER (MAIL, PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, ACTIVE, PHOTO_PATH, PAYMENT_INFO_ID, VEHICLE_ID)
VALUES ( 'nikola.nikolic@gmail.com', 'sifra', 'Nikola', 'Nikolic', '+38164047952', 'Novi Sad', TRUE, 'nikola1.png', '984d405d-4ead-43d7-8171-ae69d4a7c08f', '6f513eca-b592-4e88-9cb4-ad9c14301ab2');

INSERT INTO PAYMENT_INFO (ID, ACCOUNT_NUMBER) VALUES ('8d5abce3-bf23-40ed-a41b-54ebadd91820', '32435-3242525-23432');
INSERT INTO PAYMENT_INFO (ID, ACCOUNT_NUMBER) VALUES ('a28a4953-003b-4557-a40d-2af5eab2ab2c', '12391-2123445-00942');
INSERT INTO PAYMENT_INFO (ID, ACCOUNT_NUMBER) VALUES ('814af-48f60193-90e-b005-c23bf46e2c04', '43243-9438483-20092');

INSERT INTO PASSENGER (MAIL, PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, ACTIVE, PHOTO_PATH, PAYMENT_INFO_ID)
VALUES ( 'mile.miletic@gmail.com', 'sifra', 'Mile', 'Miletic', '+38164047952', 'Novi Sad', TRUE, 'mile1.png', '8d5abce3-bf23-40ed-a41b-54ebadd91820');

INSERT INTO PASSENGER (MAIL, PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, ACTIVE, PHOTO_PATH, PAYMENT_INFO_ID)
VALUES ( 'andrej.andrejevic@gmail.com', 'sifra', 'Andrej', 'Andrejevic', '+38164047952', 'Novi Sad', TRUE, 'andrej1.png', 'a28a4953-003b-4557-a40d-2af5eab2ab2c');

INSERT INTO PASSENGER (MAIL, PASSWORD, NAME, SURNAME, PHONE_NUMBER, CITY, ACTIVE, PHOTO_PATH, PAYMENT_INFO_ID)
VALUES ( 'petar.petrovic@gmail.com', 'sifra', 'Petar', 'Petrovic', '+38164047952', 'Novi Sad', TRUE, 'petar1.png', '814af-48f60193-90e-b005-c23bf46e2c04');

INSERT INTO ROUTE (ID) VALUES ('041cd3b0-4ffa-418b-9de1-4379b2ce550d');
INSERT INTO ROUTE (ID) VALUES ('a10494b2-9aa3-4581-b7bb-78fc665d86b0');
INSERT INTO ROUTE (ID) VALUES ('8de29ec6-ba94-4837-9be0-bdb92fd5e05e');

INSERT INTO ROUTE_LOCATIONS (ROUTE_ID, LOCATIONS_ID) VALUES ('041cd3b0-4ffa-418b-9de1-4379b2ce550d', 'b74b7c57-4542-471f-8539-4559e9489997');
INSERT INTO ROUTE_LOCATIONS (ROUTE_ID, LOCATIONS_ID) VALUES ('041cd3b0-4ffa-418b-9de1-4379b2ce550d', 'ed726dda-44c8-4953-9e21-8ce41ed4d370');
INSERT INTO ROUTE_LOCATIONS (ROUTE_ID, LOCATIONS_ID) VALUES ('a10494b2-9aa3-4581-b7bb-78fc665d86b0', 'e16c8018-c069-4aef-b315-a00327efff36');

INSERT INTO RIDE (ID, ROUTE_ID, FARE, DRIVER_MAIL, START_TIME, END_TIME, RIDE_STATUS)
VALUES ( '8107614c-04d9-480d-8a59-e1999d9e7bfc', '8de29ec6-ba94-4837-9be0-bdb92fd5e05e', 2500.0, 'zdravko.zdravkovic@gmail.com', '2022-06-22 11:05:29.267235', '2022-06-23 11:05:29.267235', 4);

INSERT INTO RIDE_PASSENGERS (RIDE_ID, PASSENGERS_MAIL)
VALUES ('8107614c-04d9-480d-8a59-e1999d9e7bfc', 'petar.petrovic@gmail.com');