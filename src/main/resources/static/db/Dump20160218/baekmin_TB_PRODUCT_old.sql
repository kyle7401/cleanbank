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
-- Table structure for table `TB_PRODUCT_old`
--

DROP TABLE IF EXISTS `TB_PRODUCT_old`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TB_PRODUCT_old` (
  `ID` int(11) NOT NULL DEFAULT '0',
  `DEL_YN` varchar(1) DEFAULT NULL COMMENT '삭제여부',
  `EVT_NM` varchar(30) DEFAULT NULL COMMENT '이벤트명',
  `PD_DESC` varchar(100) DEFAULT NULL COMMENT '설명',
  `PD_LVL1` varchar(2) NOT NULL COMMENT '품목상위코드',
  `PD_LVL2` varchar(2) NOT NULL COMMENT '품목중위코드',
  `PD_LVL3` varchar(2) NOT NULL COMMENT '품목하위코드',
  `PD_NM` varchar(100) DEFAULT NULL COMMENT '품목명',
  `PD_PRICE` int(11) DEFAULT NULL COMMENT '가격',
  `PD_SORT` int(11) DEFAULT NULL COMMENT '정렬순서',
  `REGI_DT` datetime DEFAULT NULL COMMENT '작성일자',
  `USER` varchar(255) DEFAULT NULL,
  `PD_PER` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_PRODUCT_old`
--

LOCK TABLES `TB_PRODUCT_old` WRITE;
/*!40000 ALTER TABLE `TB_PRODUCT_old` DISABLE KEYS */;
INSERT INTO `TB_PRODUCT_old` VALUES (1,'','','','01','00','00','비즈니스',NULL,NULL,NULL,'',NULL),(2,'','','','02','00','00','학생',NULL,NULL,NULL,'',NULL),(3,'','','','03','00','00','상의',NULL,NULL,NULL,'',NULL),(4,'','','','03','00','00','하의',NULL,NULL,NULL,'',NULL),(5,'','','','04','00','00','아우터',NULL,NULL,'2015-11-24 16:43:56','anonymousUser',NULL),(6,'','','','01','01','00','와이셔츠',NULL,NULL,'2015-11-24 16:46:02','anonymousUser',NULL),(7,'','','','01','02','00','남방',NULL,NULL,'2015-11-24 16:46:02','anonymousUser',NULL),(8,'','','','01','03','00','정장상의',NULL,NULL,'2015-11-24 16:46:02','anonymousUser',NULL),(9,'','','','01','04','00','정장하의',NULL,NULL,'2015-11-24 16:46:02','anonymousUser',NULL),(10,'','','','01','04','00','정장한벌',NULL,NULL,'2015-11-24 16:46:02','anonymousUser',NULL),(11,'','','일반1','01','01','01','일반',1000,1,'2015-11-24 00:00:00','anonymousUser',123),(12,'','','','01','01','02','프리미엄',2000,2,'2015-11-24 00:00:00','anonymousUser',2),(13,'','','','01','02','01','일반',1000,1,'2015-11-24 00:00:00','anonymousUser',3),(14,'','','','01','02','02','프리미엄',2000,2,'2015-11-24 00:00:00','anonymousUser',4);
/*!40000 ALTER TABLE `TB_PRODUCT_old` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-18 11:17:42
