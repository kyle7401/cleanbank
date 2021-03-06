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
-- Table structure for table `TB_LOCATION`
--

DROP TABLE IF EXISTS `TB_LOCATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TB_LOCATION` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEL_YN` varchar(255) DEFAULT NULL,
  `EVT_NM` varchar(255) DEFAULT NULL,
  `LOC1` varchar(255) DEFAULT NULL,
  `LOC2` varchar(255) DEFAULT NULL,
  `LOC3` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `REGI_DT` datetime DEFAULT NULL,
  `USER` varchar(255) DEFAULT NULL,
  `SORT_ORDER` int(11) DEFAULT NULL,
  `LO_DESC` varchar(200) DEFAULT NULL COMMENT '설명',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_LOCATION`
--

LOCK TABLES `TB_LOCATION` WRITE;
/*!40000 ALTER TABLE `TB_LOCATION` DISABLE KEYS */;
INSERT INTO `TB_LOCATION` VALUES (1,NULL,NULL,'01','00','00','서울특별시','2016-03-22 17:07:21','hyoseop',1,NULL),(2,NULL,NULL,'01','01','00','강서구','2016-03-22 17:07:21','hyoseop',1,NULL),(3,NULL,NULL,'01','01','01','염창동','2016-03-22 17:07:22','hyoseop',2,NULL),(4,NULL,NULL,'01','01','02','등촌1동','2016-03-22 17:07:22','hyoseop',2,NULL),(5,NULL,NULL,'01','01','03','등촌2동','2016-03-22 17:07:22','hyoseop',2,NULL),(6,NULL,NULL,'01','01','04','등촌3동','2016-03-22 17:07:22','hyoseop',2,NULL),(7,NULL,NULL,'01','01','05','화곡본동','2016-03-22 17:07:22','hyoseop',2,NULL),(8,NULL,NULL,'01','01','06','화곡1동','2016-03-22 17:07:22','hyoseop',2,NULL),(9,NULL,NULL,'01','01','07','화곡2동','2016-03-22 17:07:22','hyoseop',2,NULL),(10,NULL,NULL,'01','01','08','화곡3동','2016-03-22 17:07:22','hyoseop',2,NULL),(11,NULL,NULL,'01','01','09','화곡4동','2016-03-22 17:07:22','hyoseop',2,NULL),(12,NULL,NULL,'01','01','10','화곡6동','2016-03-22 17:07:22','hyoseop',2,NULL),(13,NULL,NULL,'01','01','11','화곡8동','2016-03-22 17:07:22','hyoseop',2,NULL),(14,NULL,NULL,'01','01','12','우장산동','2016-03-22 17:07:22','hyoseop',2,NULL),(15,NULL,NULL,'01','01','13','가양1동','2016-03-22 17:07:22','hyoseop',2,NULL),(16,NULL,NULL,'01','01','14','가양2동','2016-03-22 17:07:22','hyoseop',2,NULL),(17,NULL,NULL,'01','01','15','가양3동','2016-03-22 17:07:22','hyoseop',2,NULL),(18,NULL,NULL,'01','01','16','발산1동','2016-03-22 17:07:22','hyoseop',2,NULL),(19,NULL,NULL,'01','01','17','공항동','2016-03-22 17:07:22','hyoseop',2,NULL),(20,NULL,NULL,'01','01','18','방화1동','2016-03-22 17:07:22','hyoseop',2,NULL),(21,NULL,NULL,'01','01','19','방화2동','2016-03-22 17:07:22','hyoseop',2,NULL),(22,NULL,NULL,'01','01','20','방화3동','2016-03-22 17:07:22','hyoseop',2,NULL),(23,NULL,NULL,'01','02','00','양천구','2016-03-22 17:07:22','hyoseop',1,NULL),(24,NULL,NULL,'01','02','01','목1동','2016-03-22 17:07:22','hyoseop',2,NULL),(25,NULL,NULL,'01','02','02','목2동','2016-03-22 17:07:22','hyoseop',2,NULL),(26,NULL,NULL,'01','02','03','목3동','2016-03-22 17:07:22','hyoseop',2,NULL),(27,NULL,NULL,'01','02','04','목4동','2016-03-22 17:07:22','hyoseop',2,NULL),(28,NULL,NULL,'01','02','05','목5동','2016-03-22 17:07:23','hyoseop',2,NULL),(29,NULL,NULL,'01','02','06','신월1동','2016-03-22 17:07:23','hyoseop',2,NULL),(30,NULL,NULL,'01','02','07','신월2동','2016-03-22 17:07:23','hyoseop',2,NULL),(31,NULL,NULL,'01','02','08','신월3동','2016-03-22 17:07:23','hyoseop',2,NULL),(32,NULL,NULL,'01','02','09','신월4동','2016-03-22 17:07:23','hyoseop',2,NULL),(33,NULL,NULL,'01','02','10','신월5동','2016-03-22 17:07:23','hyoseop',2,NULL),(34,NULL,NULL,'01','02','11','신월6동','2016-03-22 17:07:23','hyoseop',2,NULL),(35,NULL,NULL,'01','02','12','신월7동','2016-03-22 17:07:23','hyoseop',2,NULL),(36,NULL,NULL,'01','02','13','신정1동','2016-03-22 17:07:23','hyoseop',2,NULL),(37,NULL,NULL,'01','02','14','신정2동','2016-03-22 17:07:23','hyoseop',2,NULL),(38,NULL,NULL,'01','02','15','신정3동','2016-03-22 17:07:23','hyoseop',2,NULL),(39,NULL,NULL,'01','02','16','신정4동','2016-03-22 17:07:23','hyoseop',2,NULL),(40,NULL,NULL,'01','02','17','신정6동','2016-03-22 17:07:23','hyoseop',2,NULL),(41,NULL,NULL,'01','02','18','신정7동','2016-03-22 17:07:23','hyoseop',2,NULL);
/*!40000 ALTER TABLE `TB_LOCATION` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-23 11:07:09
