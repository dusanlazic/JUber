CREATE CONSTANT DRIVER_ZDRAVKO_ID VALUE '909dccc3-4f61-4237-b3a2-6e674edd8d52';
CREATE CONSTANT DRIVER_MARKO_ID VALUE '2173891a-8bdc-4b4d-b6fb-4d790fc4630f';
CREATE CONSTANT DRIVER_NIKOLA_ID VALUE '6bc99aaf-cc8b-4d80-a7d2-7457c39b278a';
CREATE CONSTANT DRIVER_BRANKO_ID VALUE 'd06c3392-ce00-4865-aa4a-896395bf9ed7';
CREATE CONSTANT DRIVER_DUSAN_ID VALUE '9372cf69-0265-49b2-a001-f03ea7446770';

CREATE CONSTANT PASSENGER_MILE_ID VALUE '92348c29-e3cb-4c8f-ad5c-f31bf14db84d';
CREATE CONSTANT PASSENGER_ANDREJ_ID VALUE '6d34f2c5-32f1-47d9-9a8e-d4dd613b9cc1';
CREATE CONSTANT PASSENGER_PETAR_ID VALUE '6aebc916-dd04-4674-a4f2-99edec0a1811';
CREATE CONSTANT PASSENGER_DRAGAN_ID VALUE '336bc999-8285-4cc2-9d9a-007e535566a6';
CREATE CONSTANT PASSENGER_BRANIMIR_ID VALUE '0dca6adc-dc37-453e-ab42-083ca865f7fc';
CREATE CONSTANT PASSENGER_DZAMAL_ID VALUE ' 0bc98102-919e-43ef-8ead-048ac34a9736';


create constant RIDE_1 value '8107614c-04d9-480d-8a59-e1999d9e7bfc';
create constant RIDE_2 value '46ab4aff-d171-4447-a05c-204a29d0fde1';
create constant RIDE_3 value '3afa6238-862b-417b-9a88-fbf2bc90c09d';
create constant RIDE_4 value '2ac4bc01-6326-418f-a3f9-4244e3922439';
create constant RIDE_5 value '9549159f-8338-43fb-8c0f-285e79053ce8';
create constant RIDE_6 value '060443d3-abc6-458c-9f76-92e3de51a713';
create constant RIDE_7 value '7a1255b3-e69d-40f5-990d-bdfbe60e8258';
create constant RIDE_8 value 'eb3e2dfe-2373-4a2c-8f70-a12b375f49a0';
create constant RIDE_101 value 'e2435a63-1b0b-43d4-8c4f-2efc809e8917';
create constant RIDE_102 value '150b87d2-f3f0-4571-ba36-b1ed1f4698b5';
create constant RIDE_103 value '2d670d1d-1331-4929-9033-2c3dfd5853b8';
create constant RIDE_104 value '1fd4af36-cff1-4c32-94f3-5510e2bae612';
create constant RIDE_105 value 'f0a62123-35aa-49f7-8aab-adb6f7ffdeb0';
create constant RIDE_106 value 'ff8fda2f-c372-4a78-95ba-a55352a1a990';

INSERT INTO VEHICLE_TYPE(ID, NAME, PRICE) VALUES
('7b4f82de-8e90-47eb-bae6-be020c45df31', 'Hatchback', 200.00),
('920e64a8-50d7-42e0-90bd-714b48ab8e57', 'Estate', 250.00),
('7bf754a2-7907-4b47-8b66-8269cb7da37e', 'Limousine', 1000.00),
('388f2683-d7e6-40df-a8a8-0fee1f9ceb64', 'Sports', 500.00),
('3b2ed711-ffeb-4841-82ed-f967db5d875a', 'Pickup', 350.00);

INSERT INTO VEHICLE (ID, LATITUDE, LONGITUDE, BABY_FRIENDLY, PET_FRIENDLY, CAPACITY, VEHICLE_TYPE_ID) VALUES
('3d2becd5-aa6f-4410-9058-bed41a017457', 45.246, 19.8512, FALSE, TRUE, 4, '7b4f82de-8e90-47eb-bae6-be020c45df31'),
('e63dbc27-1a36-4f73-b024-07e19ea91838', 45.246, 19.8513, FALSE, FALSE, 4, '7bf754a2-7907-4b47-8b66-8269cb7da37e'),
('bb2da238-03e1-4f3c-b041-7242c7501ab3', 45.246, 19.8514, TRUE, TRUE, 7, '920e64a8-50d7-42e0-90bd-714b48ab8e57'),
('6f513eca-b592-4e88-9cb4-ad9c14301ab2', 45.246, 19.8514, TRUE, TRUE, 7, '920e64a8-50d7-42e0-90bd-714b48ab8e57'),
('0bd27920-8b09-4979-a1e5-3e9a72a2132b', 45.246, 19.8512, FALSE, TRUE, 4, '7b4f82de-8e90-47eb-bae6-be020c45df31');

-- PASSWORD = 'cascaded'
INSERT INTO DRIVER (ID, EMAIL, EMAIL_VERIFIED, IMAGE_URL, NAME, PASSWORD, PROVIDER, PROVIDER_ID, ROLE, CITY, FIRST_NAME, LAST_NAME, PHONE_NUMBER, STATUS, VEHICLE_ID, BLOCKED) VALUES
(DRIVER_ZDRAVKO_ID, 'zdravko.zdravkovic@gmail.com', TRUE, NULL, 'Zdravko Zdravkovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_DRIVER', 'Novi Sad', 'Zdravko', 'Zdravkovic', '+38164548952', 'INACTIVE', '3d2becd5-aa6f-4410-9058-bed41a017457', FALSE),
(DRIVER_MARKO_ID, 'marko.markovic@gmail.com', TRUE, NULL, 'Marko Markovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_DRIVER', 'Novi Sad', 'Marko', 'Markovic', '+38164432452', 'INACTIVE', 'e63dbc27-1a36-4f73-b024-07e19ea91838', FALSE),
(DRIVER_BRANKO_ID, 'branko.brankovic@gmail.com', TRUE, NULL, 'Branko Brankovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_DRIVER', 'Novi Sad', 'Branko', 'Brankovic', '+38165047953', 'ACTIVE', 'bb2da238-03e1-4f3c-b041-7242c7501ab3', FALSE),
(DRIVER_NIKOLA_ID, 'nikola.nikolic@gmail.com', TRUE, NULL, 'Nikola Nikolic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_DRIVER', 'Novi Sad', 'Nikola', 'Nikolic', '+38164047952', 'OVERTIME', '6f513eca-b592-4e88-9cb4-ad9c14301ab2', FALSE),
(DRIVER_DUSAN_ID, 'dusan.dusanovic@gmail.com', TRUE, NULL, 'Dusan Dusanovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_DRIVER', 'Novi Sad', 'Dusan', 'Dusanovic', '+38164548952', 'INACTIVE', '0bd27920-8b09-4979-a1e5-3e9a72a2132b', FALSE);

INSERT INTO ADMIN(ID, EMAIL, EMAIL_VERIFIED, IMAGE_URL, NAME, PASSWORD, PROVIDER, PROVIDER_ID, ROLE) VALUES
('e3661c31-d1a4-47ab-94b6-1c6500dccf24', 'admin@juber.com', TRUE, NULL, 'JUber Admin', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_ADMIN');

-- PASSWORD = 'cascaded'
INSERT INTO PASSENGER (ID, EMAIL, EMAIL_VERIFIED, IMAGE_URL, NAME, PASSWORD, PROVIDER, PROVIDER_ID, ROLE, CITY, FIRST_NAME, LAST_NAME, PHONE_NUMBER, BALANCE, BLOCKED, NOTE) VALUES
(PASSENGER_MILE_ID, 'mile.miletic@gmail.com', TRUE, NULL, 'Mile Miletic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Mile', 'Miletic', '+38164047952', '10000', FALSE, ''),
(PASSENGER_ANDREJ_ID, 'andrej.andrejevic@gmail.com', TRUE, NULL, 'Andrej Andrejevic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Andrej', 'Andrejevic', '+38164047952', '10000', FALSE, ''),
(PASSENGER_PETAR_ID, 'petar.petrovic@gmail.com', TRUE, NULL, 'Petar Petrovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Petar', 'Petrovic', '+38164047952', '10000', FALSE, ''),
(PASSENGER_DRAGAN_ID, 'dragan.draganovic@gmail.com', TRUE, NULL, 'Dragan Draganović', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Dragan', 'Draganović', '+38164047959', '10000', TRUE, 'Neće da veže pojas'),
(PASSENGER_BRANIMIR_ID, 'branimir.branimirovic@gmail.com', TRUE, NULL, 'Branimir Branimirovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Branimir', 'Branimirovic', '+38164047952', '10000', FALSE, ''),
(PASSENGER_DZAMAL_ID, 'dzamal.malik@gmail.com', TRUE, NULL, 'Dzamal Malik', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Dzamal', 'Malik', '+38164047952', '1', FALSE, '');




INSERT INTO RIDE (ID, FARE, DRIVER_ID, START_TIME, END_TIME, RIDE_STATUS, DISTANCE, ESTIMATED_TIME) VALUES
    (RIDE_1, 2500.0, DRIVER_ZDRAVKO_ID, '2023-06-22 11:05:29.267235', '2023-06-23 11:25:29.267235', 5, 100, 20),
    (RIDE_2, 2600.0, DRIVER_ZDRAVKO_ID, '2023-06-23 10:05:29.267235', '2023-06-23 10:25:29.267235', 5, 200, 20),
    (RIDE_3, 2600.0, DRIVER_ZDRAVKO_ID, '2023-06-23 10:05:29.267235', null, 3, 200, 20),
    (RIDE_4, 2600.0, DRIVER_MARKO_ID, null, null, 2, 200, 20),
    (RIDE_5, 2600.0, DRIVER_MARKO_ID, null, null, 2, 200, 20),
    (RIDE_6, 2600.0, DRIVER_NIKOLA_ID, null, null, 2, 200, 20),
    (RIDE_7, 2600.0, DRIVER_NIKOLA_ID, null, null, 6, 200, 20),
    (RIDE_8, 2600.0, DRIVER_DUSAN_ID, '2023-06-23 10:05:29.267235', null, 3, 200, 25),
    (RIDE_101, 3000.0, DRIVER_BRANKO_ID, '2023-05-22 11:05:29.267235', '2023-05-22 11:25:29.267235', 5, 100, 20),
    (RIDE_102, 3000.0, DRIVER_BRANKO_ID, '2023-05-23 11:05:29.267235', '2023-05-23 11:25:29.267235', 5, 100, 20),
    (RIDE_103, 3000.0, DRIVER_BRANKO_ID, '2023-05-24 11:05:29.267235', '2023-05-24 11:25:29.267235', 5, 100, 20),
    (RIDE_104, 3000.0, DRIVER_BRANKO_ID, '2023-05-25 11:05:29.267235', '2023-05-25 11:25:29.267235', 5, 100, 20),
    (RIDE_105, 3000.0, DRIVER_BRANKO_ID, '2023-05-26 11:05:29.267235', '2023-05-26 11:25:29.267235', 5, 100, 20),
    (RIDE_106, 3000.0, DRIVER_BRANKO_ID, '2023-05-27 11:05:29.267235', '2023-05-27 11:25:29.267235', 5, 100, 20);


INSERT INTO PLACE (ID, NAME, OPTION, LATITUDE, LONGITUDE, RIDE_ID) VALUES
('0ba05d41-6756-48a6-bd4b-ad768807f98c', 'Dr Ivana Ribara 13', '', 45.24237307826045, 19.84377035416157, RIDE_1 ),
('81a03927-95de-4a74-af13-956d0ccf77c6', 'Maksima Gorkog X', 'via Bulevar Oslobodjenja', 45.248859722424925, 19.84330204124692, RIDE_1),
('e85e9639-4f01-4e92-a3ed-863d5de001d5', 'Dr Ivana Ribara 13', '', 45.24237307826045, 19.84377035416157, RIDE_8 ),
('1b652814-ac9d-49f9-b504-1c82094347c2', 'Maksima Gorkog X', 'via Bulevar Oslobodjenja', 45.248859722424925, 19.84330204124692, RIDE_8),
('6cde875d-73b7-4bf5-991f-cb04036de829', 'Dr Ivana Ribara 13', '', 45.24237307826045, 19.84377035416157, RIDE_3 ),
('2cb388ea-54ab-4312-b960-19d6bef0bc5b', 'Maksima Gorkog X', 'via Bulevar Oslobodjenja', 45.248859722424925, 19.84330204124692, RIDE_3),

('0aecaffd-62da-43b5-8c62-80335f887f14', 'TEST Ivana Ribara 13', '', 45.24237307826045, 19.84377035416157, RIDE_2 ),
('64903e9a-abd3-4432-8c22-b81d28520784', 'TEST Maksima Gorkog X', 'via Bulevar Oslobodjenja', 45.248859722424925, 19.84330204124692, RIDE_2);

INSERT INTO ROUTE (ID, NAME, DISTANCE, DURATION, COORDINATES_ENCODED, PLACE_ID, SELECTED) VALUES
('041cd3b0-4ffa-418b-9de1-4379b2ce550d', 'route1', 0.0, 0.0, 'alcsGovbxBfBSz@lEkEdB}F|CoXxLqCiT', '81a03927-95de-4a74-af13-956d0ccf77c6', TRUE),
('a10494b2-9aa3-4581-b7bb-78fc665d86b0', 'route2', 0.0, 0.0, 'alcsGovbxBfBSz@lEkEdB}F|CoXxLqCiT', '81a03927-95de-4a74-af13-956d0ccf77c6', FALSE),
('8de29ec6-ba94-4837-9be0-bdb92fd5e05e', 'route3', 0.0, 0.0, 'alcsGovbxBfBSz@lEkEdB}F|CoXxLqCiT', '81a03927-95de-4a74-af13-956d0ccf77c6', FALSE);

INSERT INTO RIDE_PASSENGERS (RIDE_ID, PASSENGERS_ID) VALUES
(RIDE_1, PASSENGER_PETAR_ID),
(RIDE_2, PASSENGER_PETAR_ID),
(RIDE_3, PASSENGER_MILE_ID),
(RIDE_4, PASSENGER_PETAR_ID),
(RIDE_6, PASSENGER_DRAGAN_ID),
(RIDE_101, PASSENGER_MILE_ID),
(RIDE_102, PASSENGER_MILE_ID),
(RIDE_103, PASSENGER_PETAR_ID),
(RIDE_104, PASSENGER_PETAR_ID),
(RIDE_105, PASSENGER_DRAGAN_ID),
(RIDE_106, PASSENGER_DRAGAN_ID);


INSERT INTO DRIVER_RIDES( RIDES_ID, DRIVER_ID) VALUES 
( RIDE_1, '909dccc3-4f61-4237-b3a2-6e674edd8d52'),
(RIDE_2, '909dccc3-4f61-4237-b3a2-6e674edd8d52');

INSERT INTO DRIVER_SHIFT (ID, START_OF_SHIFT, END_OF_SHIFT, DURATION) VALUES
('75150af6-aa36-4f58-927a-008445fd7be9', {ts '2023-01-01 08:14:00.742000000'}, NULL, 0L);

INSERT INTO DRIVER_DRIVER_SHIFTS(DRIVER_ID, DRIVER_SHIFTS_ID) VALUES
('6bc99aaf-cc8b-4d80-a7d2-7457c39b278a', '75150af6-aa36-4f58-927a-008445fd7be9');

-- receiver: PASSENGER petar
INSERT INTO PERSISTED_NOTIFICATION(ID, CREATED, STATUS, RECEIVER_ID, DTYPE, RIDE_ID, BALANCE, CANCELER_ID, INVITEE_ID, INVITER_ID, RESPONSE, STARTING_LOCATION, START_TIME) VALUES
('0676bcc1-ed94-4f8e-9b24-5c90b571b77b', '2023-01-23 23:54:29.267235', 'UNREAD', PASSENGER_PETAR_ID, 'DriverArrivedNotification', RIDE_1,  NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('d2608eb7-a9eb-4408-b014-188f08cb628f', '2023-01-23 20:56:29.267235', 'READ', PASSENGER_PETAR_ID, 'RideInvitationNotification', RIDE_1,  1000, PASSENGER_PETAR_ID, '92348c29-e3cb-4c8f-ad5c-f31bf14db84d', '92348c29-e3cb-4c8f-ad5c-f31bf14db84d', 'NO_RESPONSE', NULL, NULL);

INSERT INTO CHAT_CONVERSATION(ID, USER_ID, SUPPORT_ID, LAST_MESSAGE_SENT_AT, IS_ARCHIVED) VALUES
('7bc42dc8-d4bf-4bdd-8a43-51b946c81c91', PASSENGER_PETAR_ID, 'e3661c31-d1a4-47ab-94b6-1c6500dccf24', '2023-01-19 18:56:29.267235', FALSE);

INSERT INTO PERSISTED_CHAT_MESSAGE(ID, CONVERSATION_ID, CONTENT, IS_FROM_SUPPORT, SENT_AT) VALUES
('be878a09-978c-430e-a1d1-a06535c219b2', '7bc42dc8-d4bf-4bdd-8a43-51b946c81c91', 'TEST_CONTENT', TRUE, '2023-01-19 18:50:29.267235');

INSERT INTO PROFILE_CHANGE_REQUEST(ID, REQUESTED_AT, STATUS, PERSON_ID) VALUES
('7f8446c6-44b3-4ea5-a887-734a059f7f76', '2023-01-15 20:56:29.267235', 'PENDING', PASSENGER_PETAR_ID);

INSERT INTO PROFILE_CHANGE_REQUEST_CHANGES(PROFILE_CHANGE_REQUEST_ID, CHANGES, CHANGES_KEY) VALUES
('7f8446c6-44b3-4ea5-a887-734a059f7f76', 'Petrović', 'lastName'),
('7f8446c6-44b3-4ea5-a887-734a059f7f76', '+1290381920', 'phoneNumber');

INSERT INTO RIDE_REVIEW(ID, COMMENT, DRIVER_RATING, VEHICLE_RATING, REVIEWER_ID, RIDE_ID) VALUES
('5b8a3cf1-2a16-4624-b989-143e92c2f054', 'Testing reviews', 5.0, 4.5, '92348c29-e3cb-4c8f-ad5c-f31bf14db84d', RIDE_1),
('afc7e19a-fdd4-483f-8893-3ff3f74050fe', 'Testing some more', 5.0, 4.5, '6d34f2c5-32f1-47d9-9a8e-d4dd613b9cc1', RIDE_1);

DROP CONSTANT DRIVER_ZDRAVKO_ID;
DROP CONSTANT DRIVER_MARKO_ID;
DROP CONSTANT DRIVER_NIKOLA_ID;
DROP CONSTANT DRIVER_BRANKO_ID;
DROP CONSTANT PASSENGER_MILE_ID;
DROP CONSTANT PASSENGER_ANDREJ_ID;
DROP CONSTANT PASSENGER_PETAR_ID;
DROP CONSTANT PASSENGER_DRAGAN_ID;
DROP CONSTANT PASSENGER_DZAMAL_ID;
DROP CONSTANT PASSENGER_BRANIMIR_ID;
DROP constant RIDE_1;
DROP constant RIDE_2;
DROP constant RIDE_3;
DROP constant RIDE_4;
DROP constant RIDE_5;
DROP constant RIDE_6;
DROP constant RIDE_7;
DROP constant RIDE_8;
DROP constant RIDE_101;
DROP constant RIDE_102;
DROP constant RIDE_103;
DROP constant RIDE_104;
DROP constant RIDE_105;
DROP constant RIDE_106;