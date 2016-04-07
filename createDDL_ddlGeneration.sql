CREATE TABLE PERSONAL_INFORMATIONS (EMAIL VARCHAR(255) NOT NULL, BIRTH_DATE TIMESTAMP, FIRST_NAME VARCHAR(255), LAST_NAME VARCHAR(255), PRIMARY KEY (EMAIL))
CREATE TABLE PLAYER (ID  SERIAL NOT NULL, NICK_NAME VARCHAR(255) UNIQUE, EMAIL VARCHAR(255) UNIQUE, PRIMARY KEY (ID))
ALTER TABLE PLAYER ADD CONSTRAINT FK_PLAYER_EMAIL FOREIGN KEY (EMAIL) REFERENCES PERSONAL_INFORMATIONS (EMAIL)
