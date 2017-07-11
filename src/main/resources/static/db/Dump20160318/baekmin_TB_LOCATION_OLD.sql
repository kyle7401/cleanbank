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
-- Table structure for table `TB_LOCATION_OLD`
--

DROP TABLE IF EXISTS `TB_LOCATION_OLD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TB_LOCATION_OLD` (
  `ID` int(11) NOT NULL DEFAULT '0',
  `DEL_YN` varchar(255) DEFAULT NULL,
  `EVT_NM` varchar(255) DEFAULT NULL,
  `LOC1` varchar(255) DEFAULT NULL,
  `LOC2` varchar(255) DEFAULT NULL,
  `LOC3` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `REGI_DT` datetime DEFAULT NULL,
  `USER` varchar(255) DEFAULT NULL,
  `SORT_ORDER` int(11) DEFAULT NULL,
  `LO_DESC` varchar(200) DEFAULT NULL COMMENT '설명'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_LOCATION_OLD`
--

LOCK TABLES `TB_LOCATION_OLD` WRITE;
/*!40000 ALTER TABLE `TB_LOCATION_OLD` DISABLE KEYS */;
INSERT INTO `TB_LOCATION_OLD` VALUES (1,'','','01','00','00','서울특별시','2015-11-30 00:00:00','hyoseop',1,NULL),(2,'','','01','01','00','강남구','2015-11-30 00:00:00','hyoseop',1,'서울 강남구'),(3,NULL,NULL,'01','02','00','강동구','2015-11-30 22:22:00','hyoseop',1,NULL),(4,'','','01','03','00','강북구','2015-11-30 00:00:00','hyoseop',1,'서울 강북구'),(5,'','','01','04','00','강서구','2015-11-30 00:00:00','hyoseop',1,''),(6,NULL,NULL,'01','05','00','관악구','2015-11-30 22:22:00','hyoseop',1,NULL),(7,NULL,NULL,'01','06','00','광진구','2015-11-30 22:22:00','hyoseop',1,NULL),(8,NULL,NULL,'01','07','00','구로구','2015-11-30 22:22:00','hyoseop',1,NULL),(9,NULL,NULL,'01','08','00','금천구','2015-11-30 22:22:00','hyoseop',1,NULL),(10,NULL,NULL,'01','09','00','노원구','2015-11-30 22:22:00','hyoseop',1,NULL),(11,NULL,NULL,'01','10','00','도봉구','2015-11-30 22:22:00','hyoseop',1,NULL),(12,NULL,NULL,'01','11','00','동대문구','2015-11-30 22:22:00','hyoseop',1,NULL),(13,NULL,NULL,'01','12','00','동작구','2015-11-30 22:22:00','hyoseop',1,NULL),(14,NULL,NULL,'01','13','00','마포구','2015-11-30 22:22:00','hyoseop',1,NULL),(15,NULL,NULL,'01','14','00','서대문구','2015-11-30 22:22:00','hyoseop',1,NULL),(16,NULL,NULL,'01','15','00','서초구','2015-11-30 22:22:00','hyoseop',1,NULL),(17,NULL,NULL,'01','16','00','성동구','2015-11-30 22:22:00','hyoseop',1,NULL),(18,NULL,NULL,'01','17','00','성북구','2015-11-30 22:22:01','hyoseop',1,NULL),(19,NULL,NULL,'01','18','00','송파구','2015-11-30 22:22:01','hyoseop',1,NULL),(20,NULL,NULL,'01','19','00','양천구','2015-11-30 22:22:01','hyoseop',1,NULL),(21,NULL,NULL,'01','20','00','영등포구','2015-11-30 22:22:01','hyoseop',1,NULL),(22,NULL,NULL,'01','21','00','용산구','2015-11-30 22:22:01','hyoseop',1,NULL),(23,NULL,NULL,'01','22','00','은평구','2015-11-30 22:22:01','hyoseop',1,NULL),(24,NULL,NULL,'01','23','00','종로구','2015-11-30 22:22:01','hyoseop',1,NULL),(25,NULL,NULL,'01','24','00','중구','2015-11-30 22:22:01','hyoseop',1,NULL),(26,NULL,NULL,'01','25','00','중랑구','2015-11-30 22:22:01','hyoseop',1,NULL),(27,NULL,NULL,'02','00','00','부산광역시','2015-11-30 22:22:01','hyoseop',2,NULL),(28,NULL,NULL,'02','01','00','동구','2015-11-30 22:22:01','hyoseop',2,NULL),(29,NULL,NULL,'02','02','00','영도구','2015-11-30 22:22:01','hyoseop',2,NULL),(30,NULL,NULL,'02','03','00','부산진구','2015-11-30 22:22:01','hyoseop',2,NULL),(31,NULL,NULL,'02','04','00','동래구','2015-11-30 22:22:01','hyoseop',2,NULL),(32,NULL,NULL,'02','05','00','서구','2015-11-30 22:22:01','hyoseop',2,NULL),(33,NULL,NULL,'02','06','00','남구','2015-11-30 22:22:01','hyoseop',2,NULL),(34,NULL,NULL,'02','07','00','북구','2015-11-30 22:22:02','hyoseop',2,NULL),(35,NULL,NULL,'02','08','00','해운대구','2015-11-30 22:22:02','hyoseop',2,NULL),(36,NULL,NULL,'02','09','00','금정구','2015-11-30 22:22:02','hyoseop',2,NULL),(37,NULL,NULL,'02','10','00','강서구','2015-11-30 22:22:02','hyoseop',2,NULL),(38,NULL,NULL,'02','11','00','연제구','2015-11-30 22:22:02','hyoseop',2,NULL),(39,NULL,NULL,'02','12','00','수영구','2015-11-30 22:22:02','hyoseop',2,NULL),(40,NULL,NULL,'02','13','00','사상구','2015-11-30 22:22:02','hyoseop',2,NULL),(41,NULL,NULL,'02','14','00','기장군','2015-11-30 22:22:02','hyoseop',2,NULL),(42,NULL,NULL,'02','15','00','중구','2015-11-30 22:22:02','hyoseop',2,NULL),(43,NULL,NULL,'02','16','00','사하구','2015-11-30 22:22:02','hyoseop',2,NULL),(44,NULL,NULL,'03','00','00','대구광역시','2015-11-30 22:22:02','hyoseop',3,NULL),(45,NULL,NULL,'03','01','00','중구','2015-11-30 22:22:02','hyoseop',3,NULL),(46,NULL,NULL,'03','02','00','동구','2015-11-30 22:22:02','hyoseop',3,NULL),(47,NULL,NULL,'03','03','00','서구','2015-11-30 22:22:02','hyoseop',3,NULL),(48,NULL,NULL,'03','04','00','남구','2015-11-30 22:22:02','hyoseop',3,NULL),(49,NULL,NULL,'03','05','00','달서구','2015-11-30 22:22:02','hyoseop',3,NULL),(50,NULL,NULL,'03','06','00','달성군','2015-11-30 22:22:02','hyoseop',3,NULL),(51,NULL,NULL,'03','07','00','북구','2015-11-30 22:22:03','hyoseop',3,NULL),(52,NULL,NULL,'03','08','00','수성구','2015-11-30 22:22:03','hyoseop',3,NULL),(53,NULL,NULL,'04','00','00','인천광역시','2015-11-30 22:22:03','hyoseop',4,NULL),(54,NULL,NULL,'04','01','00','동구','2015-11-30 22:22:03','hyoseop',4,NULL),(55,NULL,NULL,'04','02','00','남구','2015-11-30 22:22:03','hyoseop',4,NULL),(56,NULL,NULL,'04','03','00','연수구','2015-11-30 22:22:03','hyoseop',4,NULL),(57,NULL,NULL,'04','04','00','남동구','2015-11-30 22:22:03','hyoseop',4,NULL),(58,NULL,NULL,'04','05','00','계양구','2015-11-30 22:22:03','hyoseop',4,NULL),(59,NULL,NULL,'04','06','00','서구','2015-11-30 22:22:03','hyoseop',4,NULL),(60,NULL,NULL,'04','07','00','강화군','2015-11-30 22:22:03','hyoseop',4,NULL),(61,NULL,NULL,'04','08','00','옹진군','2015-11-30 22:22:03','hyoseop',4,NULL),(62,NULL,NULL,'04','09','00','중구','2015-11-30 22:22:03','hyoseop',4,NULL),(63,NULL,NULL,'04','10','00','부평구','2015-11-30 22:22:03','hyoseop',4,NULL),(64,NULL,NULL,'05','00','00','대전광역시','2015-11-30 22:22:03','hyoseop',5,NULL),(65,NULL,NULL,'05','01','00','동구','2015-11-30 22:22:03','hyoseop',5,NULL),(66,NULL,NULL,'05','02','00','중구','2015-11-30 22:22:03','hyoseop',5,NULL),(67,NULL,NULL,'05','03','00','서구','2015-11-30 22:22:03','hyoseop',5,NULL),(68,NULL,NULL,'05','04','00','유성구','2015-11-30 22:22:04','hyoseop',5,NULL),(69,NULL,NULL,'05','05','00','대덕구','2015-11-30 22:22:04','hyoseop',5,NULL),(70,NULL,NULL,'06','00','00','광주광역시','2015-11-30 22:22:04','hyoseop',6,NULL),(71,NULL,NULL,'06','01','00','동구','2015-11-30 22:22:04','hyoseop',6,NULL),(72,NULL,NULL,'06','02','00','서구','2015-11-30 22:22:04','hyoseop',6,NULL),(73,NULL,NULL,'06','03','00','남구','2015-11-30 22:22:04','hyoseop',6,NULL),(74,NULL,NULL,'06','04','00','북구','2015-11-30 22:22:04','hyoseop',6,NULL),(75,NULL,NULL,'06','05','00','광산구','2015-11-30 22:22:04','hyoseop',6,NULL),(76,NULL,NULL,'07','00','00','울산광역시','2015-11-30 22:22:04','hyoseop',7,NULL),(77,NULL,NULL,'07','01','00','중구','2015-11-30 22:22:04','hyoseop',7,NULL),(78,NULL,NULL,'07','02','00','동구','2015-11-30 22:22:04','hyoseop',7,NULL),(79,NULL,NULL,'07','03','00','울주군','2015-11-30 22:22:04','hyoseop',7,NULL),(80,NULL,NULL,'07','04','00','남구','2015-11-30 22:22:04','hyoseop',7,NULL),(81,NULL,NULL,'07','05','00','북구','2015-11-30 22:22:04','hyoseop',7,NULL),(82,NULL,NULL,'11','00','00','경기도','2015-11-30 22:22:04','hyoseop',8,NULL),(83,NULL,NULL,'11','01','00','파주시','2015-11-30 22:22:04','hyoseop',8,NULL),(84,NULL,NULL,'11','02','00','수원시','2015-11-30 22:22:04','hyoseop',9,NULL),(85,NULL,NULL,'11','02','01','장안구','2015-11-30 22:22:05','hyoseop',9,NULL),(86,NULL,NULL,'11','02','02','권선구','2015-11-30 22:22:05','hyoseop',9,NULL),(87,NULL,NULL,'11','02','03','팔달구','2015-11-30 22:22:05','hyoseop',9,NULL),(88,NULL,NULL,'11','02','04','영통구','2015-11-30 22:22:05','hyoseop',9,NULL),(89,NULL,NULL,'11','03','00','성남시','2015-11-30 22:22:05','hyoseop',10,NULL),(90,NULL,NULL,'11','03','01','수정구','2015-11-30 22:22:05','hyoseop',10,NULL),(91,NULL,NULL,'11','03','02','중원구','2015-11-30 22:22:05','hyoseop',10,NULL),(92,NULL,NULL,'11','03','03','분당구','2015-11-30 22:22:05','hyoseop',10,NULL),(93,NULL,NULL,'11','04','00','부천시','2015-11-30 22:22:05','hyoseop',11,NULL),(94,NULL,NULL,'11','04','01','원미구','2015-11-30 22:22:05','hyoseop',11,NULL),(95,NULL,NULL,'11','04','02','소사구','2015-11-30 22:22:05','hyoseop',11,NULL),(96,NULL,NULL,'11','04','03','오정구','2015-11-30 22:22:05','hyoseop',11,NULL),(97,NULL,NULL,'11','05','00','안양시','2015-11-30 22:22:05','hyoseop',12,NULL),(98,NULL,NULL,'11','05','01','만안구','2015-11-30 22:22:05','hyoseop',12,NULL),(99,NULL,NULL,'11','05','02','동안구','2015-11-30 22:22:05','hyoseop',12,NULL),(100,NULL,NULL,'11','06','00','고양시','2015-11-30 22:22:05','hyoseop',13,NULL),(101,NULL,NULL,'11','06','01','덕양구','2015-11-30 22:22:06','hyoseop',13,NULL),(102,NULL,NULL,'11','06','02','일산동구','2015-11-30 22:22:06','hyoseop',13,NULL),(103,NULL,NULL,'11','06','03','일산서구','2015-11-30 22:22:06','hyoseop',13,NULL),(104,NULL,NULL,'11','07','00','용인시','2015-11-30 22:22:06','hyoseop',14,NULL),(105,NULL,NULL,'11','07','01','처인구','2015-11-30 22:22:06','hyoseop',14,NULL),(106,NULL,NULL,'11','07','02','기흥구','2015-11-30 22:22:06','hyoseop',14,NULL),(107,NULL,NULL,'11','07','03','수지구','2015-11-30 22:22:06','hyoseop',14,NULL),(108,NULL,NULL,'11','08','00','안산시','2015-11-30 22:22:06','hyoseop',15,NULL),(109,NULL,NULL,'11','08','01','상록구','2015-11-30 22:22:06','hyoseop',15,NULL),(110,NULL,NULL,'11','08','02','단원구','2015-11-30 22:22:06','hyoseop',15,NULL),(111,NULL,NULL,'11','09','00','화성시','2015-11-30 22:22:06','hyoseop',16,NULL),(112,NULL,NULL,'11','10','00','광명시','2015-11-30 22:22:06','hyoseop',17,NULL),(113,NULL,NULL,'11','11','00','평택시','2015-11-30 22:22:06','hyoseop',18,NULL),(114,NULL,NULL,'11','12','00','이천시','2015-11-30 22:22:06','hyoseop',19,NULL),(115,NULL,NULL,'11','13','00','동두천시','2015-11-30 22:22:06','hyoseop',20,NULL),(116,NULL,NULL,'11','14','00','안성시','2015-11-30 22:22:06','hyoseop',21,NULL),(117,NULL,NULL,'11','15','00','과천시','2015-11-30 22:22:07','hyoseop',22,NULL),(118,NULL,NULL,'11','16','00','구리시','2015-11-30 22:22:07','hyoseop',23,NULL),(119,NULL,NULL,'11','17','00','남양주시','2015-11-30 22:22:07','hyoseop',24,NULL),(120,NULL,NULL,'11','18','00','오산시','2015-11-30 22:22:07','hyoseop',25,NULL),(121,NULL,NULL,'11','19','00','시흥시','2015-11-30 22:22:07','hyoseop',26,NULL),(122,NULL,NULL,'11','20','00','군포시','2015-11-30 22:22:07','hyoseop',27,NULL),(123,NULL,NULL,'11','21','00','의왕시','2015-11-30 22:22:07','hyoseop',28,NULL),(124,NULL,NULL,'11','22','00','하남시','2015-11-30 22:22:07','hyoseop',29,NULL),(125,NULL,NULL,'11','23','00','김포시','2015-11-30 22:22:07','hyoseop',30,NULL),(126,NULL,NULL,'11','24','00','광주시','2015-11-30 22:22:07','hyoseop',31,NULL),(127,NULL,NULL,'11','25','00','포천시','2015-11-30 22:22:07','hyoseop',32,NULL),(128,NULL,NULL,'11','26','00','양주시','2015-11-30 22:22:07','hyoseop',33,NULL),(129,NULL,NULL,'11','27','00','의정부시','2015-11-30 22:22:07','hyoseop',34,NULL),(130,NULL,NULL,'11','28','00','여주시','2015-11-30 22:22:07','hyoseop',35,NULL),(131,NULL,NULL,'11','29','00','연천군','2015-11-30 22:22:07','hyoseop',36,NULL),(132,NULL,NULL,'11','30','00','가평군','2015-11-30 22:22:07','hyoseop',37,NULL),(133,NULL,NULL,'11','31','00','양평군','2015-11-30 22:22:08','hyoseop',38,NULL),(134,NULL,NULL,'21','00','00','강원도','2015-11-30 22:22:08','hyoseop',39,NULL),(135,NULL,NULL,'31','00','00','충청북도','2015-11-30 22:22:08','hyoseop',40,NULL),(136,NULL,NULL,'41','00','00','충청남도','2015-11-30 22:22:08','hyoseop',41,NULL),(137,NULL,NULL,'51','00','00','전라북도','2015-11-30 22:22:08','hyoseop',42,NULL),(138,NULL,NULL,'61','00','00','전라남도','2015-11-30 22:22:08','hyoseop',43,NULL),(139,NULL,NULL,'71','00','00','경상북도','2015-11-30 22:22:08','hyoseop',44,NULL),(140,NULL,NULL,'81','00','00','경상남도','2015-11-30 22:22:08','hyoseop',45,NULL),(141,NULL,NULL,'91','00','00','제주도','2015-11-30 22:22:08','hyoseop',46,NULL),(142,'','','01','01','01','역삼1동','2016-03-03 00:00:00','(주)시냅스테크놀로지',1,'역삼동'),(143,'','','01','01','02','삼성1동','2016-03-04 00:00:00','(주)시냅스테크놀로지',2,'삼성동'),(144,'','','01','01','03','신사동','2016-03-04 00:00:00','(주)시냅스테크놀로지',3,'');
/*!40000 ALTER TABLE `TB_LOCATION_OLD` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-18 12:55:12
