-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cleanbank
-- ------------------------------------------------------
-- Server version	5.5.47-0+deb7u1

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
  `EP_DEVICE_TOKEN` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`EP_CD`),
  UNIQUE KEY `UK_qkqa8c6gyqyr33mgnd08qmkvw` (`EP_EMAIL`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='코디정보';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_EMPLOYEE`
--

LOCK TABLES `TB_EMPLOYEE` WRITE;
/*!40000 ALTER TABLE `TB_EMPLOYEE` DISABLE KEYS */;
INSERT INTO `TB_EMPLOYEE` VALUES (1,4,'','0903','test1','/emp_img/emp_img1.png','test','2015-11-19 00:00:00','0801','110301','코디 테스트1','0701','$2a$10$fiVNIzKsmkrKAJyGj1xpOumPPz8n4U4Eng/DU0ZwYNEU7xv/Jara6','M','010-1234-5678','',NULL,'anonymousUser',NULL),(2,4,'','0901','test2','/emp_img/emp_img2.jpg','2','2015-11-21 00:00:00','0801','110802','테스트2','0702','$2a$10$8k.aAJgra1RZzEE36C1kGOxL.qPwkxEz9m4RsB1rBg4Cyp0fW5PzC','M','02-1234-5678','','2015-11-19 00:00:00','anonymousUser',NULL),(3,4,'','0901','1234','/emp_img/emp_img3.png','','2015-12-05 00:00:00','0801','110202','1234','0701','$2a$10$soGcUtTES/FpseQJnu46FeC82Ya8bI/0H89zwl/mITog7IMKEcOPu','M','01012345678','','2015-12-05 00:00:00','anonymousUser',NULL),(4,1,'','0903','123556','/emp_img/emp_img4.png','','2015-12-09 00:00:00','0801','110601','123124214','0702','$2a$10$XH93kU96YoMmk6/KP3Nk2uxlCCuRgmuKI2.baMO2BU6gQkZ11XmTi','F','1231223123213','','2015-12-05 10:13:28','anonymousUser',NULL),(5,1,'','0901','asdf','/emp_img/emp_img5.jpg','','2015-12-12 00:00:00','0801','110202','asdf','0701','$2a$10$3qwrFDErx4umzyzkiCdiTu8jGGZtTOy/qOI3EOTnctimKixgznRBS','M','123123','','2015-12-05 00:00:00','anonymousUser',NULL),(6,4,'','0901','daniel.lee','/emp_img/emp_img6.jpg','열심히 달리겠습니다.','2015-12-06 00:00:00','0801','','이광훈','0701','$2a$10$rQscHflpjDpP.y/JW4Hy5e5w2ijTWuLgxOrLIKNr/hUBeMesCOX9S','M','010.2629.2627','','2015-12-06 00:00:00','anonymousUser',NULL),(7,3,'','0901','직원22','/emp_img/emp_img7.jpg','','2015-12-14 00:00:00','0801','110202','테스트','0701','$2a$10$2BazNrXkyOeESzEJB/Dq6ekLf3Ko9Ayhh4Rlsl4M5bJYpCtOtGEGm','M','','','2015-12-14 18:24:26','anonymousUser',NULL),(8,5,'','0901','ghddbfk1','/emp_img/emp_img8.jpg','백의민족입니다.','2016-02-11 00:00:00','0801','','홍유라','0701','$2a$10$jyI3mDQV9M9BAL50JjO7meuzO9pfu4AWz6v5GWrVNDPCaonIM6i5q','F','010519595','','2016-02-11 00:00:00','(주)시냅스테크놀로지',NULL),(9,4,'','0901','contdi','/emp_img/emp_img9.jpg','','2016-02-17 00:00:00','0801','110502','코디','0701','$2a$10$.TuMSLXNTpE18UunKl8F5ecU5aOm5SNldaNjm669b.QxYSUK4JCaW','M','010-3242-5642','','2016-02-17 15:46:12','(주)시냅스테크놀로지',NULL),(10,4,'','0901','kineo2k@gmail.com','/emp_img/emp_img10.JPG','','2016-02-22 00:00:00','0801','110201','지오아빠코디','0702','$2a$10$YqxODVhVRmz8Z/cj2XSlyOofdtQqNUb91dFYFQSo/UbzgyslAhtsm','M','01030437063','','2016-02-22 00:00:00','(주)시냅스테크놀로지',NULL),(11,5,'','0901','100min','/emp_img/emp_img11.jpg','','2016-02-26 00:00:00','0801','110301','codi','0701','$2a$10$0Ac4hE4PfEsAIIzBD2F5LOY1qvr8NrG5V3mc5d.pa2upFJaMJcPCO','M','','','2016-02-26 00:00:00','(주)시냅스테크놀로지',NULL),(12,5,'','0901','codi','/emp_img/emp_img12.png','11','2016-03-04 00:00:00','0801','010103','유라','0702','$2a$10$FO.OHfnk63H0tJtUUUbsJOAuQ8TH4CTYO5ziUOeslogVSIxVwjo.O','M','01055012520','','2016-03-04 00:00:00','(주)시냅스테크놀로지',NULL),(13,5,'','0901','codi2','/emp_img/emp_img13.png','111','2016-03-09 00:00:00','0801','010103','홍유라2','0701','$2a$10$cfYqf/Pz50vHGtdF6u8V5ev.EfyNXU4U1zDWVxsFS4gbqzU8VJnj2','F','01055012520','','2016-03-09 11:32:36','(주)시냅스테크놀로지',NULL),(14,4,'','0901','gunny1981','/emp_img/emp_img14.png','','2016-03-10 00:00:00','0801','','이경도','0702','$2a$10$KFYlqUcj8uvsbaXU5LNBVOCtWp7lXlDPhYC/UwMDOxyS4qY9L0.LW','M','01074351031','','2016-03-10 12:59:10','(주)시냅스테크놀로지',NULL),(15,4,'','0901','100min1','/emp_img/emp_img15.png','','2016-03-10 00:00:00','0801','','백민코디1','0701','$2a$10$cDzGt1525VmFpEv/PjUCke4EbGCjHkS3V8T4shpQSDHLV35ZdeFVq','M','010-1234-1010','','2016-03-10 16:28:37','이경도',NULL),(16,5,'','0901','codi222','/emp_img/emp_img16.png','코디입니다','2016-03-14 00:00:00','0801','010102','코디123','0702','$2a$10$Q23N/wpc/dscuX9.d834Uu4Z4xFTdvGOnwAxTu840Cb7xLYp5QD5S','F','01079179612','','2016-03-14 12:02:50','(주)시냅스테크놀로지',NULL),(17,4,'','0901','ienkyou7@naver.com','/emp_img/emp_img17.jpg','','2016-03-20 00:00:00','0801','','이인규','0701','$2a$10$GoQV8veY1UKMWSnUi9g/NObVVcEUJGiwK3OpCTUtAQRvg7lJkyCNO','M','010-2009-2052','','2016-03-20 00:00:00','(주)시냅스테크놀로지',NULL);
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

-- Dump completed on 2016-03-23 11:07:12
