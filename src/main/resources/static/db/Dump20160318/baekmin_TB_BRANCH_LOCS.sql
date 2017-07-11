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
-- Table structure for table `TB_BRANCH_LOCS`
--

DROP TABLE IF EXISTS `TB_BRANCH_LOCS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TB_BRANCH_LOCS` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BL_CD` varchar(6) NOT NULL COMMENT '지역코드',
  `BL_NM` varchar(20) DEFAULT NULL COMMENT '지역명',
  `BN_CD` int(11) NOT NULL COMMENT '지점코드',
  `DEL_YN` varchar(1) DEFAULT NULL COMMENT '삭제여부',
  `EVT_NM` varchar(30) DEFAULT NULL COMMENT '이벤트명',
  `REGI_DT` datetime DEFAULT NULL COMMENT '작성일자',
  `USER` varchar(20) DEFAULT NULL COMMENT '작성자',
  `BL_MEMO` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='지점담당지역';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_BRANCH_LOCS`
--

LOCK TABLES `TB_BRANCH_LOCS` WRITE;
/*!40000 ALTER TABLE `TB_BRANCH_LOCS` DISABLE KEYS */;
INSERT INTO `TB_BRANCH_LOCS` VALUES (2,'110301','수정구',1,'','',NULL,'anonymousUser','a11111'),(3,'110203','팔달구',1,'','','2015-12-01 15:49:11','anonymousUser','2\r\n2\r\n2\r\n2\r\n2'),(4,'110201','장안구',1,'','','2015-12-28 20:19:54','(주)시냅스테크놀로지',''),(5,'010401','염창동',4,'Y','',NULL,'(주)시냅스테크놀로지',''),(6,'110201','장안구',4,'Y','','2016-03-03 11:09:59','(주)시냅스테크놀로지','2동'),(7,'010402','등촌1동',4,'Y','',NULL,'(주)시냅스테크놀로지',''),(8,'110301','수정구',5,'','','2016-03-03 13:19:07','(주)시냅스테크놀로지',''),(9,'010101','역삼1동',5,'','',NULL,'(주)시냅스테크놀로지',''),(10,'010103','신사동',5,'','','2016-03-04 13:22:14','(주)시냅스테크놀로지',''),(11,'010102','삼성1동',5,'','',NULL,'(주)시냅스테크놀로지','삼성동 '),(12,'010401','염창동',4,'','','2016-03-15 18:51:03','(주)시냅스테크놀로지',''),(13,'010402','등촌1동',4,'','','2016-03-15 18:51:10','(주)시냅스테크놀로지',''),(14,'010403','등촌2동',4,'','','2016-03-15 18:51:17','(주)시냅스테크놀로지',''),(15,'010411','화곡8동',4,'','','2016-03-15 18:51:27','(주)시냅스테크놀로지',''),(16,'010409','화곡4동',4,'','','2016-03-15 18:51:34','(주)시냅스테크놀로지',''),(17,'011904','목4동',4,'','','2016-03-16 12:49:35','(주)시냅스테크놀로지','');
/*!40000 ALTER TABLE `TB_BRANCH_LOCS` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-18 12:55:08
