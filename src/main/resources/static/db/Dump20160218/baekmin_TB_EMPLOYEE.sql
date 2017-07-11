-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cleanbank
-- ------------------------------------------------------
-- Server version	5.7.10

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `TB_EMPLOYEE`
--

DROP TABLE IF EXISTS `TB_EMPLOYEE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TB_EMPLOYEE` (
  `EP_CD` int(11) NOT NULL AUTO_INCREMENT COMMENT '코디코드',
  `BN_CD` int(11) DEFAULT NULL COMMENT '지점코드',
  `DEL_YN` varchar(1) DEFAULT NULL COMMENT '삭제여부',
  `EP_DRIVE_YN` varchar(4) DEFAULT NULL COMMENT '운전능력',
  `EP_EMAIL` varchar(100) DEFAULT NULL COMMENT '코드아이디',
  `EP_IMG` varchar(100) DEFAULT NULL COMMENT '사진',
  `EP_INTRO` varchar(4000) DEFAULT NULL COMMENT '자기소개',
  `EP_JOIN_DT` datetime DEFAULT NULL COMMENT '가입일자',
  `EP_LEVEL` varchar(4) DEFAULT NULL COMMENT '코디등급',
  `EP_LOC` varchar(6) DEFAULT NULL COMMENT '담당지역',
  `EP_NM` varchar(20) DEFAULT NULL COMMENT '성명',
  `EP_PART` varchar(4) DEFAULT NULL COMMENT '담당업무',
  `EP_PASS` varchar(100) DEFAULT NULL COMMENT '암호',
  `EP_SEX` varchar(4) DEFAULT NULL COMMENT '성별',
  `EP_TEL` varchar(50) DEFAULT NULL COMMENT '연락처',
  `EVT_NM` varchar(30) DEFAULT NULL COMMENT '이벤트명',
  `REGI_DT` datetime DEFAULT NULL COMMENT '작성일자',
  `USER` varchar(20) DEFAULT NULL COMMENT '작성자',
  PRIMARY KEY (`EP_CD`),
  UNIQUE KEY `UK_qkqa8c6gyqyr33mgnd08qmkvw` (`EP_EMAIL`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='코디정보';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_EMPLOYEE`
--

LOCK TABLES `TB_EMPLOYEE` WRITE;
/*!40000 ALTER TABLE `TB_EMPLOYEE` DISABLE KEYS */;
INSERT INTO `TB_EMPLOYEE` VALUES (1,4,'','0903','test1','/emp_img/emp_img1.png','test','2015-11-19 00:00:00','0801','110301','테스트1','0701','$2a$10$qkX45B/8ZiMig/GiK9DEbeJo0DYzvEI/8IlhbwbQ7Coy1gpTNITD6','M','010-1234-5678','',NULL,'anonymousUser'),(2,4,'','0901','test2','/emp_img/emp_img2.jpg','2','2015-11-21 00:00:00','0801','110802','테스트2','0702','$2a$10$8k.aAJgra1RZzEE36C1kGOxL.qPwkxEz9m4RsB1rBg4Cyp0fW5PzC','M','02-1234-5678','','2015-11-19 00:00:00','anonymousUser'),(3,2,'','0901','1234','/emp_img/emp_img3.png','','2015-12-05 00:00:00','0801','110202','1234','0701','$2a$10$EuLIP4P4jHYVgTL553fuH./h.B9lhaqyqPX8bL7XQ0q01jCHN.3Wu','M','01012345678','','2015-12-05 00:00:00','anonymousUser'),(4,1,'','0903','123556','/emp_img/emp_img4.png','','2015-12-09 00:00:00','0801','110601','123124214','0702','$2a$10$XH93kU96YoMmk6/KP3Nk2uxlCCuRgmuKI2.baMO2BU6gQkZ11XmTi','F','1231223123213','','2015-12-05 10:13:28','anonymousUser'),(5,1,'','0901','asdf','/emp_img/emp_img5.jpg','','2015-12-12 00:00:00','0801','110202','asdf','0701','$2a$10$3qwrFDErx4umzyzkiCdiTu8jGGZtTOy/qOI3EOTnctimKixgznRBS','M','123123','','2015-12-05 00:00:00','anonymousUser'),(6,1,'','0901','daniel.lee','/emp_img/emp_img6.JPG','열심히 달리겠습니다.','2015-12-06 00:00:00','0801','110702','이광훈','0701','$2a$10$kfvJchBIBEjS9g9lzInBROMNwDWXwXOn2ncfIE38DVIoIvAad4Lcu','M','010.2629.2627','','2015-12-06 21:38:56','anonymousUser'),(7,3,'','0901','직원22','/emp_img/emp_img7.jpg','','2015-12-14 00:00:00','0801','110202','테스트','0701','$2a$10$2BazNrXkyOeESzEJB/Dq6ekLf3Ko9Ayhh4Rlsl4M5bJYpCtOtGEGm','M','','','2015-12-14 18:24:26','anonymousUser'),(8,4,'','0901','ghddbfk1','/emp_img/emp_img8.jpg','백의민족입니다.','2016-02-11 00:00:00','0801','110202','홍유라','0701','$2a$10$IDMLUf8JD9j/5jo7Bg0Vg.NI6mxoTxuStVzwBkgLvnf2zIDdam3BW','F','010519595','','2016-02-11 00:00:00','(주)시냅스테크놀로지'),(9,4,'','0901','contdi','/emp_img/emp_img9.jpg','','2016-02-17 00:00:00','0801','110502','코디','0701','$2a$10$.TuMSLXNTpE18UunKl8F5ecU5aOm5SNldaNjm669b.QxYSUK4JCaW','M','010-3242-5642','','2016-02-17 15:46:12','(주)시냅스테크놀로지');
/*!40000 ALTER TABLE `TB_EMPLOYEE` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-18 11:17:49
