INSERT INTO LOCATION (ID, NAME, LONGITUDE, LATITUDE) VALUES
('b74b7c57-4542-471f-8539-4559e9489997', 'FTN', 45.246, 19.8512),
('ed726dda-44c8-4953-9e21-8ce41ed4d370', 'Knjizara', 45.253672, 19.841270),
('e16c8018-c069-4aef-b315-a00327efff36', 'Porodiliste', 45.258208, 19.829756);

INSERT INTO VEHICLE_TYPE(ID, NAME, PRICE) VALUES
('7b4f82de-8e90-47eb-bae6-be020c45df31', 'Hatchback', 200.00),
('920e64a8-50d7-42e0-90bd-714b48ab8e57', 'Estate', 250.00),
('7bf754a2-7907-4b47-8b66-8269cb7da37e', 'Limousine', 1000.00),
('388f2683-d7e6-40df-a8a8-0fee1f9ceb64', 'Sports', 500.00),
('3b2ed711-ffeb-4841-82ed-f967db5d875a', 'Pickup', 350.00);

INSERT INTO VEHICLE (ID, LONGITUDE, LATITUDE, BABY_FRIENDLY, PET_FRIENDLY, CAPACITY, VEHICLE_TYPE_ID) VALUES
('3d2becd5-aa6f-4410-9058-bed41a017457', 45.246, 19.8512, FALSE, TRUE, 4, '7b4f82de-8e90-47eb-bae6-be020c45df31'),
('e63dbc27-1a36-4f73-b024-07e19ea91838', 45.246, 19.8512, FALSE, FALSE, 4, '7bf754a2-7907-4b47-8b66-8269cb7da37e'),
('6f513eca-b592-4e88-9cb4-ad9c14301ab2', 45.246, 19.8512, TRUE, TRUE, 7, '920e64a8-50d7-42e0-90bd-714b48ab8e57');

-- PASSWORD = 'cascaded'
INSERT INTO DRIVER (ID, EMAIL, EMAIL_VERIFIED, IMAGE_URL, NAME, PASSWORD, PROVIDER, PROVIDER_ID, ROLE, CITY, FIRST_NAME, LAST_NAME, PHONE_NUMBER, ACTIVE, VEHICLE_ID) VALUES
('909dccc3-4f61-4237-b3a2-6e674edd8d52', 'zdravko.zdravkovic@gmail.com', TRUE, 'zdravko1.png', 'Zdravko Zdravkovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_DRIVER', 'Novi Sad', 'Zdravko', 'Zdravkovic', '+38164548952', FALSE, '3d2becd5-aa6f-4410-9058-bed41a017457'),
('2173891a-8bdc-4b4d-b6fb-4d790fc4630f', 'marko.markovic@gmail.com', TRUE, 'marko1.png', 'Marko Markovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_DRIVER', 'Novi Sad', 'Marko', 'Markovic', '+38164432452', FALSE, 'e63dbc27-1a36-4f73-b024-07e19ea91838'),
('6bc99aaf-cc8b-4d80-a7d2-7457c39b278a', 'nikola.nikolic@gmail.com', TRUE, 'nikola.png', 'Nikola Nikolic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_DRIVER', 'Novi Sad', 'Nikola', 'Nikolic', '+38164047952', FALSE, '6f513eca-b592-4e88-9cb4-ad9c14301ab2');

INSERT INTO ADMIN(ID, EMAIL, EMAIL_VERIFIED, IMAGE_URL, NAME, PASSWORD, PROVIDER, PROVIDER_ID, ROLE) VALUES
('e3661c31-d1a4-47ab-94b6-1c6500dccf24', 'admin@juber.com', TRUE, NULL, 'JUber Admin', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_ADMIN');

-- PASSWORD = 'cascaded'
INSERT INTO PASSENGER (ID, EMAIL, EMAIL_VERIFIED, IMAGE_URL, NAME, PASSWORD, PROVIDER, PROVIDER_ID, ROLE, CITY, FIRST_NAME, LAST_NAME, PHONE_NUMBER, BALANCE) VALUES
('92348c29-e3cb-4c8f-ad5c-f31bf14db84d', 'mile.miletic@gmail.com', TRUE, 'mile1.png', 'Mile Miletic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Mile', 'Miletic', '+38164047952', '0'),
('6d34f2c5-32f1-47d9-9a8e-d4dd613b9cc1', 'andrej.andrejevic@gmail.com', TRUE, 'andrej1.png', 'Andrej Andrejevic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Andrej', 'Andrejevic', '+38164047952', '0'),
('6aebc916-dd04-4674-a4f2-99edec0a1811', 'petar.petrovic@gmail.com', TRUE, 'petar1.png', 'Petar Petrovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Petar', 'Petrovic', '+38164047952', '0');

INSERT INTO ROUTE (ID) VALUES
('041cd3b0-4ffa-418b-9de1-4379b2ce550d'),
('a10494b2-9aa3-4581-b7bb-78fc665d86b0'),
('8de29ec6-ba94-4837-9be0-bdb92fd5e05e');

INSERT INTO ROUTE_LOCATIONS (ROUTE_ID, LOCATIONS_ID) VALUES
('041cd3b0-4ffa-418b-9de1-4379b2ce550d', 'b74b7c57-4542-471f-8539-4559e9489997'),
('041cd3b0-4ffa-418b-9de1-4379b2ce550d', 'ed726dda-44c8-4953-9e21-8ce41ed4d370'),
('a10494b2-9aa3-4581-b7bb-78fc665d86b0', 'e16c8018-c069-4aef-b315-a00327efff36');

INSERT INTO RIDE (ID, ROUTE_ID, FARE, DRIVER_ID, START_TIME, END_TIME, RIDE_STATUS) VALUES
('8107614c-04d9-480d-8a59-e1999d9e7bfc', '8de29ec6-ba94-4837-9be0-bdb92fd5e05e', 2500.0, '909dccc3-4f61-4237-b3a2-6e674edd8d52', '2022-06-22 11:05:29.267235', '2022-06-23 11:05:29.267235', 4);

INSERT INTO RIDE_PASSENGERS (RIDE_ID, PASSENGERS_ID) VALUES
('8107614c-04d9-480d-8a59-e1999d9e7bfc', '6aebc916-dd04-4674-a4f2-99edec0a1811');
