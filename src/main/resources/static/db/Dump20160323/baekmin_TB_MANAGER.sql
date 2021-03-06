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
-- Table structure for table `TB_MANAGER`
--

DROP TABLE IF EXISTS `TB_MANAGER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TB_MANAGER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BN_CD` int(11) NOT NULL COMMENT '지점코드',
  `DEL_YN` varchar(1) DEFAULT NULL COMMENT '삭제여부',
  `EVT_NM` varchar(30) DEFAULT NULL COMMENT '이벤트명',
  `MG_CD` int(11) NOT NULL COMMENT '관리자코드',
  `MG_DT` datetime DEFAULT NULL COMMENT '가입일자',
  `MG_EMAIL` varchar(100) NOT NULL COMMENT '관리자아이디',
  `MG_NM` varchar(20) DEFAULT NULL COMMENT '성명',
  `MG_PASS` varchar(100) DEFAULT NULL COMMENT '암호',
  `MG_TYPE` varchar(4) DEFAULT NULL COMMENT '관리유형',
  `REGI_DT` datetime DEFAULT NULL COMMENT '작성일자',
  `USER` varchar(20) DEFAULT NULL COMMENT '작성자',
  `MG_TEL` varchar(20) DEFAULT NULL COMMENT '연락처',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_4kpxfolgxqdgekevkwuujs3ed` (`MG_EMAIL`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='관리자정보';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_MANAGER`
--

LOCK TABLES `TB_MANAGER` WRITE;
/*!40000 ALTER TABLE `TB_MANAGER` DISABLE KEYS */;
INSERT INTO `TB_MANAGER` VALUES (1,1,'','',0,'2015-11-19 00:00:00','support@synapsetech.co.kr','(주)시냅스테크놀로지','$2a$10$QlYhw154lDBJ23iBXlk9BOSFlPztOll232d6S9BLbZkJnUw/1ImTq','0001','2015-11-19 00:00:00','anonymousUser','010-1234-5678'),(2,2,'','',0,'2015-11-30 00:00:00','100min.co.kr','백의민족','$2a$10$.nt9SX4veEkMpsq5sIhi6OSEUJwuxtDGokxLmsIfK9jTTyxaoMWOC','0001','2015-11-30 00:00:00','anonymousUser',NULL),(3,1,'','',0,'2015-12-05 00:00:00','adssad@dada.com','테스트1234','$2a$10$JxDpJpQBMaVyq7vJHcRAXuNXnYVXPRxXuxqugYyYOFEJSJ3IK14hi','0001','2015-12-05 00:00:00','anonymousUser','02-9876-5432'),(4,4,'','',0,'2016-02-11 00:00:00','gijum@gujum.co.kr','지점장','$2a$10$XlxiyNIWTMkNo97D98hFNuEZDfRhFFEqa/.6UtU5EuA4tLD4KkHxe','0002','2016-02-11 00:00:00','(주)시냅스테크놀로지','01066224422'),(5,5,'','',0,'2016-02-29 00:00:00','test1@synapsetech.co.kr','지점장 test1','$2a$10$aGNPGoUM5kYs/ZweUeehgeOJaKKOyXWLfXBNvfrBZM2oEB8dMji5S','0002','2016-02-29 00:00:00','(주)시냅스테크놀로지',''),(6,4,'','',0,'2016-03-10 00:00:00','kyungdo.lee@100min.co.kr','이경도','$2a$10$C3wrUCBUm7ECmAz1EFfL3ejXlNysO6mvAQZJM3BAOXGoAuUbhPrP.','0002','2016-03-10 16:22:11','(주)시냅스테크놀로지','01074351031'),(7,5,'','',0,'2016-03-11 00:00:00','test1','지점장1','$2a$10$aTlL7uCquEuw4HnoXX3A6eE8uvmgkd8PspRc1eAo68arxBDmyqvou','0002','2016-03-11 00:00:00','(주)시냅스테크놀로지','01055012520'),(8,5,'Y','',0,'2016-03-20 00:00:00','ienkyou7@naver.com_삭제8','이인규','$2a$10$UIcTQna2Li8ohGcthDUec.6PmjL6VbIDohgDmFjj5ShFLyHFVCKlW','0001','2016-03-20 12:54:58','(주)시냅스테크놀로지','010-2009-2052');
/*!40000 ALTER TABLE `TB_MANAGER` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-23 11:07:06
