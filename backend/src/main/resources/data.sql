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
INSERT INTO DRIVER (ID, EMAIL, EMAIL_VERIFIED, IMAGE_URL, NAME, PASSWORD, PROVIDER, PROVIDER_ID, ROLE, CITY, FIRST_NAME, LAST_NAME, PHONE_NUMBER, STATUS, VEHICLE_ID) VALUES
('909dccc3-4f61-4237-b3a2-6e674edd8d52', 'zdravko.zdravkovic@gmail.com', TRUE, 'zdravko1.png', 'Zdravko Zdravkovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_DRIVER', 'Novi Sad', 'Zdravko', 'Zdravkovic', '+38164548952', 'INACTIVE', '3d2becd5-aa6f-4410-9058-bed41a017457'),
('2173891a-8bdc-4b4d-b6fb-4d790fc4630f', 'marko.markovic@gmail.com', TRUE, 'marko1.png', 'Marko Markovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_DRIVER', 'Novi Sad', 'Marko', 'Markovic', '+38164432452', 'INACTIVE', 'e63dbc27-1a36-4f73-b024-07e19ea91838'),
('6bc99aaf-cc8b-4d80-a7d2-7457c39b278a', 'nikola.nikolic@gmail.com', TRUE, 'nikola.png', 'Nikola Nikolic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_DRIVER', 'Novi Sad', 'Nikola', 'Nikolic', '+38164047952', 'OVERTIME', '6f513eca-b592-4e88-9cb4-ad9c14301ab2');

INSERT INTO ADMIN(ID, EMAIL, EMAIL_VERIFIED, IMAGE_URL, NAME, PASSWORD, PROVIDER, PROVIDER_ID, ROLE) VALUES
('e3661c31-d1a4-47ab-94b6-1c6500dccf24', 'admin@juber.com', TRUE, NULL, 'JUber Admin', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_ADMIN');

-- PASSWORD = 'cascaded'
INSERT INTO PASSENGER (ID, EMAIL, EMAIL_VERIFIED, IMAGE_URL, NAME, PASSWORD, PROVIDER, PROVIDER_ID, ROLE, CITY, FIRST_NAME, LAST_NAME, PHONE_NUMBER, BALANCE) VALUES
('92348c29-e3cb-4c8f-ad5c-f31bf14db84d', 'mile.miletic@gmail.com', TRUE, '', 'Mile Miletic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Mile', 'Miletic', '+38164047952', '0'),
('6d34f2c5-32f1-47d9-9a8e-d4dd613b9cc1', 'andrej.andrejevic@gmail.com', TRUE, 'andrej1.png', 'Andrej Andrejevic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Andrej', 'Andrejevic', '+38164047952', '0'),
('6aebc916-dd04-4674-a4f2-99edec0a1811', 'petar.petrovic@gmail.com', TRUE, '', 'Petar Petrovic', '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'local', NULL, 'ROLE_PASSENGER','Novi Sad', 'Petar', 'Petrovic', '+38164047952', '0');


INSERT INTO RIDE (ID, FARE, DRIVER_ID, START_TIME, END_TIME, RIDE_STATUS) VALUES
    ('8107614c-04d9-480d-8a59-e1999d9e7bfc', 2500.0, '909dccc3-4f61-4237-b3a2-6e674edd8d52', '2023-06-22 11:05:29.267235', '2023-06-23 11:05:29.267235', 2);


INSERT INTO PLACE (ID, NAME, OPTION, LATITUDE, LONGITUDE, RIDE_ID) VALUES
('0ba05d41-6756-48a6-bd4b-ad768807f98c', 'Dr Ivana Ribara 13', '', 45.24237307826045, 19.84377035416157, '8107614c-04d9-480d-8a59-e1999d9e7bfc' ),
('81a03927-95de-4a74-af13-956d0ccf77c6', 'Maksima Gorkog X', 'via Bulevar Oslobodjenja', 45.248859722424925, 19.84330204124692, '8107614c-04d9-480d-8a59-e1999d9e7bfc');

INSERT INTO ROUTE (ID, NAME, DISTANCE, DURATION, COORDINATES, PLACE_ID, SELECTED) VALUES
('041cd3b0-4ffa-418b-9de1-4379b2ce550d', 'route1', 0.0, 0.0, 'alcsGovbxBfBSz@lEkEdB}F|CoXxLqCiT', '81a03927-95de-4a74-af13-956d0ccf77c6', TRUE),
('a10494b2-9aa3-4581-b7bb-78fc665d86b0', 'route2', 0.0, 0.0, 'alcsGovbxBfBSz@lEkEdB}F|CoXxLqCiT', '81a03927-95de-4a74-af13-956d0ccf77c6', FALSE),
('8de29ec6-ba94-4837-9be0-bdb92fd5e05e', 'route3', 0.0, 0.0, 'alcsGovbxBfBSz@lEkEdB}F|CoXxLqCiT', '81a03927-95de-4a74-af13-956d0ccf77c6', FALSE);



INSERT INTO RIDE_PASSENGERS (RIDE_ID, PASSENGERS_ID) VALUES
('8107614c-04d9-480d-8a59-e1999d9e7bfc', '6aebc916-dd04-4674-a4f2-99edec0a1811');

VALUES ( '8107614c-04d9-480d-8a59-e1999d9e7bfc', '041cd3b0-4ffa-418b-9de1-4379b2ce550d', 2500.0, 'zdravko.zdravkovic@gmail.com', '2022-06-22 11:05:29.267235', '2022-06-23 11:05:29.267235', 1);

INSERT INTO DRIVER_SHIFT (ID, START_OF_SHIFT, END_OF_SHIFT, DURATION) VALUES
('75150af6-aa36-4f58-927a-008445fd7be9', {ts '2023-01-01 08:14:00.742000000'}, NULL, 0L);

INSERT INTO DRIVER_DRIVER_SHIFTS(DRIVER_ID, DRIVER_SHIFTS_ID) VALUES
('6bc99aaf-cc8b-4d80-a7d2-7457c39b278a', '75150af6-aa36-4f58-927a-008445fd7be9');

-- receiver: PASSENGER petar
INSERT INTO PERSISTED_NOTIFICATION(ID, CREATED, STATUS, RECEIVER_ID, DTYPE,    RIDE_ID, BALANCE, MINUTES_LEFT, CANCELER_ID, INVITEE_ID, INVITER_ID, RESPONSE) VALUES
('0676bcc1-ed94-4f8e-9b24-5c90b571b77b', '2023-01-15 23:54:29.267235', 'UNREAD', '6aebc916-dd04-4674-a4f2-99edec0a1811', 'DriverArrivedNotification', '8107614c-04d9-480d-8a59-e1999d9e7bfc',  NULL, NULL, NULL, NULL, NULL, NULL),
('d2608eb7-a9eb-4408-b014-188f08cb628f', '2023-01-15 20:56:29.267235', 'READ', '6aebc916-dd04-4674-a4f2-99edec0a1811', 'RideInvitationNotification', '8107614c-04d9-480d-8a59-e1999d9e7bfc',  1000, NULL, NULL, '6aebc916-dd04-4674-a4f2-99edec0a1811', '92348c29-e3cb-4c8f-ad5c-f31bf14db84d', 'NO_RESPONSE');

INSERT INTO CHAT_CONVERSATION(ID, USER_ID, SUPPORT_ID, LAST_MESSAGE_SENT_AT, IS_ARCHIVED) VALUES
('7bc42dc8-d4bf-4bdd-8a43-51b946c81c91', '6aebc916-dd04-4674-a4f2-99edec0a1811', 'e3661c31-d1a4-47ab-94b6-1c6500dccf24', '2023-01-19 18:56:29.267235', FALSE);

INSERT INTO PERSISTED_CHAT_MESSAGE(ID, CONVERSATION_ID, CONTENT, IS_FROM_SUPPORT, SENT_AT) VALUES
('be878a09-978c-430e-a1d1-a06535c219b2', '7bc42dc8-d4bf-4bdd-8a43-51b946c81c91', 'TEST_CONTENT', TRUE, '2023-01-19 18:50:29.267235');

