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
-- Table structure for table `TB_MEMBER_BILLINFO`
--

DROP TABLE IF EXISTS `TB_MEMBER_BILLINFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TB_MEMBER_BILLINFO` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MB_BILLINFO` varchar(100) DEFAULT NULL,
  `MB_CD` int(11) NOT NULL,
  `MB_BIILKEY` varchar(100) NOT NULL,
  `MB_CARD_CD` varchar(10) DEFAULT NULL,
  `MB_CARD_NM` varchar(50) DEFAULT NULL,
  `MB_CARD_NO` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_MEMBER_BILLINFO`
--

LOCK TABLES `TB_MEMBER_BILLINFO` WRITE;
/*!40000 ALTER TABLE `TB_MEMBER_BILLINFO` DISABLE KEYS */;
INSERT INTO `TB_MEMBER_BILLINFO` VALUES (1,'KB Card',1,'1234-56789-ABC1',NULL,NULL,NULL),(2,'BC Card',1,'1234-56789-ABC2',NULL,NULL,NULL),(3,'ShinHan Card',1,'1234-56789-ABC3',NULL,NULL,NULL),(5,NULL,172,'5db186e23ec2a18c4da19a82bebecc9b63e684d4','06','국민','502123167004'),(6,NULL,220,'0eddb16d7d7c6cb0c6dc36af0332f3288e151c32','06','국민','502123167004'),(7,NULL,174,'90ddfc2636851ff42003e6b53210d516044ce67f','11','BC','538920000309'),(9,NULL,230,'4fdbc87f654a5052bce8166b2de2d121da7940ed','11','BC','538920000309'),(10,NULL,232,'c47efa397e480fbd56672ecb18b42de52bf85e88','06','국민','502123167004'),(11,NULL,228,'8ddd401ac2c21d450efaa00989ec99f760029fb4','06','국민','502123167004'),(12,NULL,233,'fb86095b4e9612c2f0a8ef285a8d7fd67b0f421e','12','삼성','531070400045'),(13,NULL,243,'5a76be814c53399f97ba537887567cc4c3b58ea9','11','BC','538920000309'),(14,NULL,244,'f3c34be3145297ed3de916156f7ee5e7eb571f31','06','국민','502123167004'),(15,NULL,242,'fc26df4540c1cbfb0b088a625e83591eb5e40866','06','국민','502123167004'),(34,NULL,255,'b480ac8b49f41982492c4f53b9d1499f6fb125b3','11','BC','452013000108'),(52,NULL,237,'62255f77a5a4c591ab2815d084fa5cd65e1f4fba','11','BC','452013000108'),(87,NULL,248,'8e4f6faaba783da3dcd5c7fce662e99b8bdaa4b4','12','삼성','531070400045'),(88,NULL,262,'c94ff046de5f2691f2f20b4d8bca1dee5c7a6620','11','BC','548020575655'),(100,NULL,186,'87030f9e2ef0ef2478c14b043fefba096dd36b4b','04','현대','543333045581'),(101,NULL,186,'1742c844e8d10eb18a9f55cc2e9be61db72c45e2','06','국민','457973055083'),(103,NULL,207,'a09a6291d9202be3f32cc0959672cc7ed7abc076','03','롯데','520045056472');
/*!40000 ALTER TABLE `TB_MEMBER_BILLINFO` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-23 11:07:10
