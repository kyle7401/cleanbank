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
-- Table structure for table `TB_PROMOTION_USE`
--

DROP TABLE IF EXISTS `TB_PROMOTION_USE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TB_PROMOTION_USE` (
  `PO_CD` bigint(20) DEFAULT NULL,
  `DEL_YN` varchar(255) DEFAULT NULL,
  `EVT_NM` varchar(255) DEFAULT NULL,
  `MB_CD` int(11) NOT NULL,
  `PU_USE` varchar(100) DEFAULT NULL COMMENT '사용내역 ',
  `PU_USE_DT` datetime DEFAULT NULL,
  `REGI_DT` datetime NOT NULL,
  `USER` varchar(255) NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OR_CD` bigint(20) DEFAULT NULL,
  `USE_YN` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_abu75mmnw9ohy8jqsrtik1jc0` (`PO_CD`),
  CONSTRAINT `FK_abu75mmnw9ohy8jqsrtik1jc0` FOREIGN KEY (`PO_CD`) REFERENCES `TB_PROMOTION` (`PO_CD`)
) ENGINE=InnoDB AUTO_INCREMENT=352 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_PROMOTION_USE`
--

LOCK TABLES `TB_PROMOTION_USE` WRITE;
/*!40000 ALTER TABLE `TB_PROMOTION_USE` DISABLE KEYS */;
INSERT INTO `TB_PROMOTION_USE` VALUES (3,'N','신규',1,NULL,NULL,'2016-02-11 11:06:46','모바일',1,NULL,NULL),(4,'N','수정',30,'주문코드 : 0 에서 사용','2016-02-11 11:56:17','2016-02-11 11:51:17','모바일',2,0,'Y'),(5,'N','수정',30,'주문코드 : 0 에서 사용','2016-02-11 11:56:17','2016-02-11 11:53:49','모바일',3,1,'Y'),(6,'N','수정',30,'주문코드 : 0 에서 사용','2016-02-11 11:56:17','2016-02-11 11:55:41','모바일',4,0,'Y'),(3,'N','신규',30,NULL,NULL,'2016-02-13 16:16:46','모바일',5,NULL,NULL),(7,'N','신규',30,NULL,NULL,'2016-02-13 16:19:40','모바일',6,NULL,NULL),(6,'N','신규',51,NULL,NULL,'2016-02-15 12:00:54','모바일',7,NULL,NULL),(6,'N','수정',62,'주문코드 : 0 에서 사용','2016-02-17 19:38:24','2016-02-17 19:37:28','모바일',8,0,'Y'),(5,'N','수정',62,'주문코드 : 0 에서 사용','2016-02-17 19:38:24','2016-02-17 19:38:04','모바일',9,0,'Y'),(9,'N','신규',1,NULL,NULL,'2016-02-18 10:08:13','모바일',10,NULL,NULL),(9,'N','신규',2,NULL,NULL,'2016-02-18 10:08:13','모바일',11,NULL,NULL),(9,'N','신규',3,NULL,NULL,'2016-02-18 10:08:13','모바일',12,NULL,NULL),(9,'N','신규',4,NULL,NULL,'2016-02-18 10:08:13','모바일',13,NULL,NULL),(9,'N','신규',5,NULL,NULL,'2016-02-18 10:08:13','모바일',14,NULL,NULL),(9,'N','신규',6,NULL,NULL,'2016-02-18 10:08:13','모바일',15,NULL,NULL),(9,'N','신규',7,NULL,NULL,'2016-02-18 10:08:13','모바일',16,NULL,NULL),(9,'N','신규',8,NULL,NULL,'2016-02-18 10:08:13','모바일',17,NULL,NULL),(9,'N','수정',9,'쿠폰 사용 테스트 #1','2016-02-18 14:38:36','2016-02-18 10:08:13','모바일',18,31,'Y'),(9,'N','신규',10,NULL,NULL,'2016-02-18 10:08:13','모바일',19,NULL,NULL),(9,'N','신규',11,NULL,NULL,'2016-02-18 10:08:13','모바일',20,NULL,NULL),(9,'N','신규',12,NULL,NULL,'2016-02-18 10:08:13','모바일',21,NULL,NULL),(9,'N','신규',24,NULL,NULL,'2016-02-18 10:08:13','모바일',22,NULL,NULL),(9,'N','신규',25,NULL,NULL,'2016-02-18 10:08:13','모바일',23,NULL,NULL),(9,'N','신규',26,NULL,NULL,'2016-02-18 10:08:13','모바일',24,NULL,NULL),(9,'N','신규',27,NULL,NULL,'2016-02-18 10:08:13','모바일',25,NULL,NULL),(9,'N','수정',28,'주문코드 : 0 에서 사용','2016-02-26 10:11:16','2016-02-18 10:08:13','모바일',26,0,'Y'),(9,'N','신규',52,NULL,NULL,'2016-02-18 10:08:13','모바일',27,NULL,NULL),(9,'N','신규',54,NULL,NULL,'2016-02-18 10:08:14','모바일',28,NULL,NULL),(9,'N','신규',55,NULL,NULL,'2016-02-18 10:08:14','모바일',29,NULL,NULL),(9,'N','신규',56,NULL,NULL,'2016-02-18 10:08:14','모바일',30,NULL,NULL),(9,'N','신규',59,NULL,NULL,'2016-02-18 10:08:14','모바일',31,NULL,NULL),(9,'N','신규',60,NULL,NULL,'2016-02-18 10:08:14','모바일',32,NULL,NULL),(9,'N','신규',62,NULL,NULL,'2016-02-18 10:08:14','모바일',33,NULL,NULL),(9,'N','신규',34,NULL,NULL,'2016-02-18 10:08:14','모바일',34,NULL,NULL),(9,'N','신규',35,NULL,NULL,'2016-02-18 10:08:14','모바일',35,NULL,NULL),(9,'N','신규',58,NULL,NULL,'2016-02-18 10:08:14','모바일',36,NULL,NULL),(9,'N','신규',36,NULL,NULL,'2016-02-18 10:08:14','모바일',37,NULL,NULL),(9,'N','신규',43,NULL,NULL,'2016-02-18 10:08:14','모바일',38,NULL,NULL),(9,'N','신규',47,NULL,NULL,'2016-02-18 10:08:14','모바일',39,NULL,NULL),(9,'N','수정',53,'주문코드 : 0 에서 사용','2016-02-19 14:05:37','2016-02-18 10:08:14','모바일',40,0,'Y'),(9,'N','신규',57,NULL,NULL,'2016-02-18 10:08:14','모바일',41,NULL,NULL),(9,'N','신규',61,NULL,NULL,'2016-02-18 10:08:14','모바일',42,NULL,NULL),(9,'N','신규',64,NULL,NULL,'2016-02-18 10:08:14','모바일',43,NULL,NULL),(9,'N','신규',29,NULL,NULL,'2016-02-18 10:08:14','모바일',44,NULL,NULL),(9,'N','신규',41,NULL,NULL,'2016-02-18 10:08:14','모바일',45,NULL,NULL),(9,'N','신규',22,NULL,NULL,'2016-02-18 10:08:14','모바일',46,NULL,NULL),(9,'N','신규',33,NULL,NULL,'2016-02-18 10:08:14','모바일',47,NULL,NULL),(9,'N','신규',48,NULL,NULL,'2016-02-18 10:08:14','모바일',48,NULL,NULL),(9,'N','신규',65,NULL,NULL,'2016-02-18 10:08:14','모바일',49,NULL,NULL),(9,'N','수정',42,'주문코드 : 0 에서 사용','2016-02-19 20:32:34','2016-02-18 10:08:14','모바일',50,0,'Y'),(9,'N','신규',46,NULL,NULL,'2016-02-18 10:08:14','모바일',51,NULL,NULL),(9,'N','신규',37,NULL,NULL,'2016-02-18 10:08:14','모바일',52,NULL,NULL),(9,'N','신규',14,NULL,NULL,'2016-02-18 10:08:14','모바일',53,NULL,NULL),(9,'N','신규',15,NULL,NULL,'2016-02-18 10:08:14','모바일',54,NULL,NULL),(9,'N','신규',16,NULL,NULL,'2016-02-18 10:08:14','모바일',55,NULL,NULL),(9,'N','신규',17,NULL,NULL,'2016-02-18 10:08:14','모바일',56,NULL,NULL),(9,'N','신규',18,NULL,NULL,'2016-02-18 10:08:14','모바일',57,NULL,NULL),(9,'N','신규',19,NULL,NULL,'2016-02-18 10:08:14','모바일',58,NULL,NULL),(9,'N','신규',20,NULL,NULL,'2016-02-18 10:08:14','모바일',59,NULL,NULL),(9,'N','신규',21,NULL,NULL,'2016-02-18 10:08:15','모바일',60,NULL,NULL),(9,'N','신규',23,NULL,NULL,'2016-02-18 10:08:15','모바일',61,NULL,NULL),(10,'N','수정',42,'주문코드 : 0 에서 사용','2016-02-19 20:30:20','2016-02-19 18:57:49','모바일',62,0,'Y'),(9,'N','신규',106,NULL,NULL,'2016-02-22 11:51:23','모바일',63,NULL,NULL),(13,'N','신규',1,NULL,NULL,'2016-02-23 10:25:38','모바일',64,NULL,NULL),(13,'N','신규',2,NULL,NULL,'2016-02-23 10:25:38','모바일',65,NULL,NULL),(13,'N','신규',3,NULL,NULL,'2016-02-23 10:25:38','모바일',66,NULL,NULL),(13,'N','신규',4,NULL,NULL,'2016-02-23 10:25:38','모바일',67,NULL,NULL),(13,'N','신규',5,NULL,NULL,'2016-02-23 10:25:38','모바일',68,NULL,NULL),(13,'N','신규',6,NULL,NULL,'2016-02-23 10:25:38','모바일',69,NULL,NULL),(13,'N','신규',7,NULL,NULL,'2016-02-23 10:25:38','모바일',70,NULL,NULL),(13,'N','신규',8,NULL,NULL,'2016-02-23 10:25:39','모바일',71,NULL,NULL),(13,'N','신규',9,NULL,NULL,'2016-02-23 10:25:39','모바일',72,NULL,NULL),(13,'N','신규',10,NULL,NULL,'2016-02-23 10:25:39','모바일',73,NULL,NULL),(13,'N','신규',11,NULL,NULL,'2016-02-23 10:25:39','모바일',74,NULL,NULL),(13,'N','신규',12,NULL,NULL,'2016-02-23 10:25:39','모바일',75,NULL,NULL),(13,'N','신규',24,NULL,NULL,'2016-02-23 10:25:39','모바일',76,NULL,NULL),(13,'N','신규',25,NULL,NULL,'2016-02-23 10:25:39','모바일',77,NULL,NULL),(13,'N','신규',26,NULL,NULL,'2016-02-23 10:25:39','모바일',78,NULL,NULL),(13,'N','신규',27,NULL,NULL,'2016-02-23 10:25:39','모바일',79,NULL,NULL),(13,'N','신규',28,NULL,NULL,'2016-02-23 10:25:39','모바일',80,NULL,NULL),(13,'N','신규',42,NULL,NULL,'2016-02-23 10:25:39','모바일',81,NULL,NULL),(13,'N','신규',52,NULL,NULL,'2016-02-23 10:25:39','모바일',82,NULL,NULL),(13,'N','신규',54,NULL,NULL,'2016-02-23 10:25:39','모바일',83,NULL,NULL),(13,'N','신규',55,NULL,NULL,'2016-02-23 10:25:39','모바일',84,NULL,NULL),(13,'N','신규',56,NULL,NULL,'2016-02-23 10:25:39','모바일',85,NULL,NULL),(13,'N','신규',59,NULL,NULL,'2016-02-23 10:25:39','모바일',86,NULL,NULL),(13,'N','신규',60,NULL,NULL,'2016-02-23 10:25:39','모바일',87,NULL,NULL),(13,'N','신규',62,NULL,NULL,'2016-02-23 10:25:39','모바일',88,NULL,NULL),(13,'N','신규',34,NULL,NULL,'2016-02-23 10:25:39','모바일',89,NULL,NULL),(13,'N','신규',100,NULL,NULL,'2016-02-23 10:25:39','모바일',90,NULL,NULL),(13,'N','신규',104,NULL,NULL,'2016-02-23 10:25:39','모바일',91,NULL,NULL),(13,'N','신규',35,NULL,NULL,'2016-02-23 10:25:39','모바일',92,NULL,NULL),(13,'N','신규',58,NULL,NULL,'2016-02-23 10:25:39','모바일',93,NULL,NULL),(13,'N','신규',36,NULL,NULL,'2016-02-23 10:25:39','모바일',94,NULL,NULL),(13,'N','신규',43,NULL,NULL,'2016-02-23 10:25:39','모바일',95,NULL,NULL),(13,'N','신규',108,NULL,NULL,'2016-02-23 10:25:39','모바일',96,NULL,NULL),(13,'N','신규',47,NULL,NULL,'2016-02-23 10:25:39','모바일',97,NULL,NULL),(13,'N','신규',67,NULL,NULL,'2016-02-23 10:25:39','모바일',98,NULL,NULL),(13,'N','신규',101,NULL,NULL,'2016-02-23 10:25:39','모바일',99,NULL,NULL),(13,'N','신규',102,NULL,NULL,'2016-02-23 10:25:39','모바일',100,NULL,NULL),(13,'N','신규',53,NULL,NULL,'2016-02-23 10:25:39','모바일',101,NULL,NULL),(13,'N','신규',97,NULL,NULL,'2016-02-23 10:25:39','모바일',102,NULL,NULL),(13,'N','신규',109,NULL,NULL,'2016-02-23 10:25:39','모바일',103,NULL,NULL),(13,'N','신규',111,NULL,NULL,'2016-02-23 10:25:40','모바일',104,NULL,NULL),(13,'N','신규',112,NULL,NULL,'2016-02-23 10:25:40','모바일',105,NULL,NULL),(13,'N','신규',113,NULL,NULL,'2016-02-23 10:25:40','모바일',106,NULL,NULL),(13,'N','신규',29,NULL,NULL,'2016-02-23 10:25:40','모바일',107,NULL,NULL),(13,'N','신규',41,NULL,NULL,'2016-02-23 10:25:40','모바일',108,NULL,NULL),(13,'N','신규',22,NULL,NULL,'2016-02-23 10:25:40','모바일',109,NULL,NULL),(13,'N','신규',103,NULL,NULL,'2016-02-23 10:25:40','모바일',110,NULL,NULL),(13,'N','신규',33,NULL,NULL,'2016-02-23 10:25:40','모바일',111,NULL,NULL),(13,'N','신규',105,NULL,NULL,'2016-02-23 10:25:40','모바일',112,NULL,NULL),(13,'N','신규',48,NULL,NULL,'2016-02-23 10:25:40','모바일',113,NULL,NULL),(13,'N','신규',68,NULL,NULL,'2016-02-23 10:25:40','모바일',114,NULL,NULL),(13,'N','신규',66,NULL,NULL,'2016-02-23 10:25:40','모바일',115,NULL,NULL),(13,'N','신규',98,NULL,NULL,'2016-02-23 10:25:40','모바일',116,NULL,NULL),(13,'N','신규',110,NULL,NULL,'2016-02-23 10:25:40','모바일',117,NULL,NULL),(13,'N','신규',37,NULL,NULL,'2016-02-23 10:25:40','모바일',118,NULL,NULL),(13,'N','신규',106,NULL,NULL,'2016-02-23 10:25:40','모바일',119,NULL,NULL),(13,'N','신규',14,NULL,NULL,'2016-02-23 10:25:40','모바일',120,NULL,NULL),(13,'N','신규',15,NULL,NULL,'2016-02-23 10:25:40','모바일',121,NULL,NULL),(13,'N','신규',16,NULL,NULL,'2016-02-23 10:25:40','모바일',122,NULL,NULL),(13,'N','신규',17,NULL,NULL,'2016-02-23 10:25:40','모바일',123,NULL,NULL),(13,'N','신규',18,NULL,NULL,'2016-02-23 10:25:40','모바일',124,NULL,NULL),(13,'N','신규',19,NULL,NULL,'2016-02-23 10:25:40','모바일',125,NULL,NULL),(13,'N','신규',20,NULL,NULL,'2016-02-23 10:25:40','모바일',126,NULL,NULL),(13,'N','신규',21,NULL,NULL,'2016-02-23 10:25:40','모바일',127,NULL,NULL),(13,'N','신규',23,NULL,NULL,'2016-02-23 10:25:40','모바일',128,NULL,NULL),(15,'N','수정',130,'주문코드 : 0 에서 사용','2016-02-24 09:57:30','2016-02-24 09:25:50','모바일',194,0,'Y'),(12,'N','수정',130,'주문코드 : 0 에서 사용','2016-02-24 13:52:00','2016-02-24 09:26:50','모바일',195,0,'Y'),(9,'N','신규',121,NULL,NULL,'2016-02-24 13:06:42','모바일',196,NULL,NULL),(9,'N','수정',128,'주문코드 : 0 에서 사용','2016-02-24 14:39:55','2016-02-24 14:39:08','모바일',197,0,'Y'),(16,'N','수정',128,'주문코드 : 0 에서 사용','2016-02-24 14:47:52','2016-02-24 14:47:31','모바일',198,0,'Y'),(16,'N','신규',131,NULL,NULL,'2016-02-24 15:07:13','모바일',199,NULL,NULL),(6,'N','신규',131,NULL,NULL,'2016-02-24 15:08:19','모바일',200,NULL,NULL),(16,'N','수정',130,'주문코드 : 0 에서 사용','2016-02-24 15:38:45','2016-02-24 15:20:33','모바일',201,0,'Y'),(5,'N','신규',128,NULL,NULL,'2016-02-24 15:28:00','모바일',202,NULL,NULL),(6,'N','신규',133,NULL,NULL,'2016-02-24 22:32:48','모바일',203,NULL,NULL),(16,'N','신규',134,NULL,NULL,'2016-02-25 08:11:04','모바일',204,NULL,NULL),(11,'N','수정',135,'주문코드 : 0 에서 사용','2016-02-25 13:23:14','2016-02-25 13:22:59','모바일',205,0,'Y'),(7,'N','신규',141,NULL,NULL,'2016-02-25 14:21:47','모바일',206,NULL,NULL),(6,'N','신규',141,NULL,NULL,'2016-02-25 14:23:26','모바일',207,NULL,NULL),(16,'N','신규',133,NULL,NULL,'2016-02-25 14:39:08','모바일',208,NULL,NULL),(16,'N','수정',141,'주문코드 : 0 에서 사용','2016-02-25 17:07:26','2016-02-25 14:43:13','모바일',209,0,'Y'),(16,'N','수정',162,'주문코드 : 0 에서 사용','2016-02-26 10:57:43','2016-02-25 20:54:30','모바일',210,0,'Y'),(6,'N','수정',166,'주문코드 : 0 에서 사용','2016-02-26 10:56:30','2016-02-26 10:47:24','모바일',211,0,'Y'),(17,'N','신규',169,NULL,NULL,'2016-02-26 15:09:16','모바일',212,NULL,NULL),(17,'N','신규',171,NULL,NULL,'2016-02-26 15:09:16','모바일',213,NULL,NULL),(17,'N','신규',173,NULL,NULL,'2016-02-26 15:09:16','모바일',214,NULL,NULL),(17,'N','신규',172,NULL,NULL,'2016-02-26 15:09:16','모바일',215,NULL,NULL),(17,'N','수정',174,'주문코드 : 0 에서 사용','2016-02-26 21:34:34','2016-02-26 15:09:16','모바일',216,0,'Y'),(18,'N','수정',169,'주문코드 : 0 에서 사용','2016-03-03 16:50:37','2016-02-26 15:11:23','모바일',222,0,'Y'),(18,'N','신규',171,NULL,NULL,'2016-02-26 15:11:23','모바일',223,NULL,NULL),(18,'N','신규',173,NULL,NULL,'2016-02-26 15:11:23','모바일',224,NULL,NULL),(18,'N','신규',172,NULL,NULL,'2016-02-26 15:11:23','모바일',225,NULL,NULL),(18,'N','수정',174,'주문코드 : 0 에서 사용','2016-03-02 17:46:49','2016-02-26 15:11:23','모바일',226,0,'Y'),(20,'N','신규',169,NULL,NULL,'2016-02-26 15:14:18','모바일',232,NULL,NULL),(20,'N','신규',171,NULL,NULL,'2016-02-26 15:14:18','모바일',233,NULL,NULL),(20,'N','신규',173,NULL,NULL,'2016-02-26 15:14:19','모바일',234,NULL,NULL),(20,'N','신규',172,NULL,NULL,'2016-02-26 15:14:19','모바일',235,NULL,NULL),(20,'N','수정',174,'주문코드 : 0 에서 사용','2016-03-02 17:46:49','2016-02-26 15:14:19','모바일',236,0,'Y'),(21,'N','신규',169,NULL,NULL,'2016-02-26 15:27:57','모바일',242,NULL,NULL),(21,'N','신규',171,NULL,NULL,'2016-02-26 15:27:57','모바일',243,NULL,NULL),(21,'N','신규',173,NULL,NULL,'2016-02-26 15:27:57','모바일',244,NULL,NULL),(21,'N','신규',172,NULL,NULL,'2016-02-26 15:27:57','모바일',245,NULL,NULL),(21,'N','수정',174,'주문코드 : 0 에서 사용','2016-03-02 17:47:27','2016-02-26 15:27:57','모바일',246,0,'Y'),(22,'N','신규',169,NULL,NULL,'2016-02-26 15:28:29','모바일',247,NULL,NULL),(22,'N','신규',171,NULL,NULL,'2016-02-26 15:28:29','모바일',248,NULL,NULL),(22,'N','신규',173,NULL,NULL,'2016-02-26 15:28:29','모바일',249,NULL,NULL),(22,'N','신규',172,NULL,NULL,'2016-02-26 15:28:29','모바일',250,NULL,NULL),(22,'N','수정',174,'주문코드 : 0 에서 사용','2016-02-26 17:27:17','2016-02-26 15:28:30','모바일',251,0,'Y'),(23,'N','신규',169,NULL,NULL,'2016-02-26 15:29:20','모바일',252,NULL,NULL),(23,'N','신규',171,NULL,NULL,'2016-02-26 15:29:20','모바일',253,NULL,NULL),(23,'N','신규',173,NULL,NULL,'2016-02-26 15:29:20','모바일',254,NULL,NULL),(23,'N','신규',172,NULL,NULL,'2016-02-26 15:29:20','모바일',255,NULL,NULL),(23,'N','수정',174,'주문코드 : 0 에서 사용','2016-03-02 17:47:27','2016-02-26 15:29:20','모바일',256,0,'Y'),(24,'N','신규',171,NULL,NULL,'2016-02-26 15:42:56','모바일',263,NULL,NULL),(24,'N','신규',173,NULL,NULL,'2016-02-26 15:42:56','모바일',264,NULL,NULL),(24,'N','신규',172,NULL,NULL,'2016-02-26 15:42:56','모바일',265,NULL,NULL),(24,'N','수정',174,'주문코드 : 0 에서 사용','2016-02-26 17:27:10','2016-02-26 15:42:56','모바일',266,0,'Y'),(11,'N','신규',182,NULL,NULL,'2016-02-28 15:42:24','모바일',267,NULL,NULL),(12,'N','신규',182,NULL,NULL,'2016-02-28 15:47:27','모바일',268,NULL,NULL),(14,'N','신규',182,NULL,NULL,'2016-02-28 16:49:33','모바일',269,NULL,NULL),(11,'N','신규',186,NULL,NULL,'2016-02-28 21:30:08','모바일',270,NULL,NULL),(24,NULL,NULL,169,NULL,NULL,'2016-03-02 00:00:00','모바',271,171,'Y'),(25,'N','신규',181,NULL,NULL,'2016-03-02 18:48:05','모바일',272,NULL,NULL),(25,'N','신규',184,NULL,NULL,'2016-03-02 18:48:05','모바일',273,NULL,NULL),(25,'N','수정',169,'주문코드 : 0 에서 사용','2016-03-03 16:49:49','2016-03-02 18:48:05','모바일',274,0,'Y'),(25,'N','신규',178,NULL,NULL,'2016-03-02 18:48:05','모바일',275,NULL,NULL),(25,'N','신규',191,NULL,NULL,'2016-03-02 18:48:05','모바일',276,NULL,NULL),(25,'N','신규',177,NULL,NULL,'2016-03-02 18:48:05','모바일',277,NULL,NULL),(25,'N','신규',186,NULL,NULL,'2016-03-02 18:48:05','모바일',278,NULL,NULL),(25,'N','신규',187,NULL,NULL,'2016-03-02 18:48:05','모바일',279,NULL,NULL),(25,'N','신규',189,NULL,NULL,'2016-03-02 18:48:05','모바일',280,NULL,NULL),(25,'N','신규',188,NULL,NULL,'2016-03-02 18:48:05','모바일',281,NULL,NULL),(25,'N','신규',201,NULL,NULL,'2016-03-02 18:48:05','모바일',282,NULL,NULL),(25,'N','신규',172,NULL,NULL,'2016-03-02 18:48:05','모바일',283,NULL,NULL),(25,'N','신규',174,NULL,NULL,'2016-03-02 18:48:05','모바일',284,237,'Y'),(26,'N','신규',181,NULL,NULL,'2016-03-02 18:48:41','모바일',285,NULL,NULL),(26,'N','신규',184,NULL,NULL,'2016-03-02 18:48:41','모바일',286,NULL,NULL),(26,'N','신규',169,NULL,NULL,'2016-03-02 18:48:42','모바일',287,NULL,NULL),(26,'N','신규',178,NULL,NULL,'2016-03-02 18:48:42','모바일',288,NULL,NULL),(26,'N','신규',191,NULL,NULL,'2016-03-02 18:48:42','모바일',289,NULL,NULL),(26,'N','신규',177,NULL,NULL,'2016-03-02 18:48:42','모바일',290,NULL,NULL),(26,'N','신규',186,NULL,NULL,'2016-03-02 18:48:42','모바일',291,NULL,NULL),(26,'N','신규',187,NULL,NULL,'2016-03-02 18:48:42','모바일',292,NULL,NULL),(26,'N','신규',189,NULL,NULL,'2016-03-02 18:48:42','모바일',293,NULL,NULL),(26,'N','신규',188,NULL,NULL,'2016-03-02 18:48:42','모바일',294,NULL,NULL),(26,'N','신규',201,NULL,NULL,'2016-03-02 18:48:42','모바일',295,NULL,NULL),(26,'N','신규',172,NULL,NULL,'2016-03-02 18:48:42','모바일',296,227,'Y'),(26,'N','신규',174,NULL,NULL,'2016-03-02 18:48:42','모바일',297,236,'Y'),(26,'N','신규',203,NULL,NULL,'2016-03-02 19:09:13','모바일',298,NULL,NULL),(9,'N','신규',203,NULL,NULL,'2016-03-02 19:09:37','모바일',299,NULL,NULL),(25,'N','신규',203,NULL,NULL,'2016-03-02 19:12:19','모바일',300,NULL,NULL),(25,'N','신규',204,NULL,NULL,'2016-03-02 19:17:36','모바일',301,NULL,NULL),(25,'N','신규',205,NULL,NULL,'2016-03-08 18:41:18','모바일',302,251,'Y'),(26,'N','신규',205,NULL,NULL,'2016-03-08 18:41:45','모바일',303,251,'Y'),(28,'N','신규',169,NULL,NULL,'2016-03-09 11:37:19','모바일',304,NULL,NULL),(28,'N','신규',181,NULL,NULL,'2016-03-09 11:37:19','모바일',305,NULL,NULL),(28,'N','신규',184,NULL,NULL,'2016-03-09 11:37:19','모바일',306,NULL,NULL),(28,'N','신규',202,NULL,NULL,'2016-03-09 11:37:19','모바일',307,NULL,NULL),(28,'N','신규',203,NULL,NULL,'2016-03-09 11:37:19','모바일',308,NULL,NULL),(28,'N','신규',178,NULL,NULL,'2016-03-09 11:37:19','모바일',309,NULL,NULL),(28,'N','신규',209,NULL,NULL,'2016-03-09 11:37:19','모바일',310,NULL,NULL),(28,'N','신규',211,NULL,NULL,'2016-03-09 11:37:19','모바일',311,NULL,NULL),(28,'N','신규',191,NULL,NULL,'2016-03-09 11:37:19','모바일',312,NULL,NULL),(28,'N','신규',204,NULL,NULL,'2016-03-09 11:37:19','모바일',313,NULL,NULL),(28,'N','신규',206,NULL,NULL,'2016-03-09 11:37:19','모바일',314,NULL,NULL),(28,'N','신규',177,NULL,NULL,'2016-03-09 11:37:19','모바일',315,NULL,NULL),(28,'N','신규',208,NULL,NULL,'2016-03-09 11:37:19','모바일',316,NULL,NULL),(28,'N','신규',210,NULL,NULL,'2016-03-09 11:37:19','모바일',317,NULL,NULL),(28,'N','신규',212,NULL,NULL,'2016-03-09 11:37:19','모바일',318,NULL,NULL),(28,'N','신규',207,NULL,NULL,'2016-03-09 11:37:19','모바일',319,NULL,NULL),(28,'N','신규',186,NULL,NULL,'2016-03-09 11:37:19','모바일',320,NULL,NULL),(28,'N','신규',187,NULL,NULL,'2016-03-09 11:37:19','모바일',321,NULL,NULL),(28,'N','신규',213,NULL,NULL,'2016-03-09 11:37:19','모바일',322,NULL,NULL),(28,'N','신규',189,NULL,NULL,'2016-03-09 11:37:19','모바일',323,NULL,NULL),(28,'N','신규',214,NULL,NULL,'2016-03-09 11:37:19','모바일',324,NULL,NULL),(28,'N','신규',205,NULL,NULL,'2016-03-09 11:37:19','모바일',325,NULL,NULL),(28,'N','신규',172,NULL,NULL,'2016-03-09 11:37:19','모바일',326,NULL,NULL),(28,'N','신규',174,NULL,NULL,'2016-03-09 11:37:19','모바일',327,NULL,NULL),(29,'N','신규',169,NULL,NULL,'2016-03-09 11:39:59','모바일',328,NULL,NULL),(29,'N','신규',181,NULL,NULL,'2016-03-09 11:39:59','모바일',329,NULL,NULL),(29,'N','신규',184,NULL,NULL,'2016-03-09 11:39:59','모바일',330,NULL,NULL),(29,'N','신규',202,NULL,NULL,'2016-03-09 11:39:59','모바일',331,NULL,NULL),(29,'N','신규',203,NULL,NULL,'2016-03-09 11:39:59','모바일',332,NULL,NULL),(29,'N','신규',178,NULL,NULL,'2016-03-09 11:39:59','모바일',333,NULL,NULL),(29,'N','신규',209,NULL,NULL,'2016-03-09 11:39:59','모바일',334,NULL,NULL),(29,'N','신규',211,NULL,NULL,'2016-03-09 11:39:59','모바일',335,NULL,NULL),(29,'N','신규',191,NULL,NULL,'2016-03-09 11:39:59','모바일',336,NULL,NULL),(29,'N','신규',204,NULL,NULL,'2016-03-09 11:39:59','모바일',337,NULL,NULL),(29,'N','신규',206,NULL,NULL,'2016-03-09 11:39:59','모바일',338,NULL,NULL),(29,'N','신규',177,NULL,NULL,'2016-03-09 11:39:59','모바일',339,NULL,NULL),(29,'N','신규',208,NULL,NULL,'2016-03-09 11:39:59','모바일',340,NULL,NULL),(29,'N','신규',210,NULL,NULL,'2016-03-09 11:39:59','모바일',341,NULL,NULL),(29,'N','신규',212,NULL,NULL,'2016-03-09 11:39:59','모바일',342,NULL,NULL),(29,'N','신규',207,NULL,NULL,'2016-03-09 11:39:59','모바일',343,NULL,NULL),(29,'N','신규',186,NULL,NULL,'2016-03-09 11:39:59','모바일',344,NULL,NULL),(29,'N','신규',187,NULL,NULL,'2016-03-09 11:39:59','모바일',345,NULL,NULL),(29,'N','신규',213,NULL,NULL,'2016-03-09 11:39:59','모바일',346,NULL,NULL),(29,'N','신규',189,NULL,NULL,'2016-03-09 11:39:59','모바일',347,NULL,NULL),(29,'N','신규',214,NULL,NULL,'2016-03-09 11:39:59','모바일',348,NULL,NULL),(29,'N','신규',205,NULL,NULL,'2016-03-09 11:39:59','모바일',349,NULL,NULL),(29,'N','신규',172,NULL,NULL,'2016-03-09 11:40:00','모바일',350,NULL,NULL),(29,'N','신규',174,NULL,NULL,'2016-03-09 11:40:00','모바일',351,291,'Y');
/*!40000 ALTER TABLE `TB_PROMOTION_USE` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-10 10:51:40
