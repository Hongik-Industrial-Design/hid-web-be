-- MySQL 데이터베이스 서버에 연결
USE mysql;

-- 새로운 데이터베이스 'hid'를 생성
CREATE DATABASE hid;

-- 'hid'라는 이름의 새 사용자 'hid'를 생성하고 비밀번호를 'hid0326'으로 설정
CREATE USER 'hid'@'%' IDENTIFIED BY 'hid0326';

-- 새 사용자 'hid'에게 데이터베이스 'hid'의 모든 권한을 부여
GRANT ALL PRIVILEGES ON hid.* TO 'hid'@'%';

-- 권한 변경 사항을 즉시 적용
FLUSH PRIVILEGES;

-- hid 데이터베이스 서버에 연결
USE hid;

-- 모든 데이터베이스 목록을 표시
SHOW DATABASES;

-- 데이터베이스 'hid'의 'exhibit_entity' 테이블에서 모든 데이터를 조회
SELECT * FROM hid.exhibit_entity;

-- 데이터베이스 'hid'의 'exhibit_artist_entity' 테이블에서 모든 데이터를 조회
SELECT * FROM hid.exhibit_artist_entity;
