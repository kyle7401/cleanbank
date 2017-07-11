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
-- Table structure for table `TB_PROMO_LOCATION`
--

DROP TABLE IF EXISTS `TB_PROMO_LOCATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TB_PROMO_LOCATION` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '프로모션코드',
  `PL_CD` varchar(6) NOT NULL COMMENT '지역정보',
  `DEL_YN` varchar(1) DEFAULT NULL COMMENT '삭제여부',
  `EVT_NM` varchar(30) DEFAULT NULL COMMENT '이벤트명',
  `REGI_DT` datetime DEFAULT NULL COMMENT '작성일자',
  `USER` varchar(20) DEFAULT NULL COMMENT '작성자',
  `PL_NM` varchar(100) DEFAULT NULL,
  `PO_CD` bigint(20) NOT NULL COMMENT '프로모션코드',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='프로모션지역정보';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_PROMO_LOCATION`
--

LOCK TABLES `TB_PROMO_LOCATION` WRITE;
/*!40000 ALTER TABLE `TB_PROMO_LOCATION` DISABLE KEYS */;
INSERT INTO `TB_PROMO_LOCATION` VALUES (1,'110203',NULL,'',NULL,'anonymousUser','팔달구',1),(2,'110801','','','2015-12-01 19:52:47','anonymousUser','상록구',1),(3,'110602','','','2015-12-01 20:14:05','anonymousUser','일산동구',1),(4,'110202','','','2016-02-11 16:38:57','지점장','권선구',6),(5,'110402','','',NULL,'지점장','소사구',6),(6,'110701','','',NULL,'(주)시냅스테크놀로지','처인구',9),(7,'110203','','',NULL,'(주)시냅스테크놀로지','팔달구',9);
/*!40000 ALTER TABLE `TB_PROMO_LOCATION` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-28 15:59:45
