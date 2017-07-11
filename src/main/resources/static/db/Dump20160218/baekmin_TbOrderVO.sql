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
-- Table structure for table `TbOrderVO`
--

DROP TABLE IF EXISTS `TbOrderVO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TbOrderVO` (
  `OR_CD` bigint(20) NOT NULL AUTO_INCREMENT,
  `BN_CD` int(11) DEFAULT NULL,
  `DEL_YN` varchar(1) NOT NULL,
  `EP_CD` int(11) DEFAULT NULL,
  `EVT_NM` varchar(30) DEFAULT NULL,
  `MB_CD` int(11) DEFAULT NULL,
  `OR_ACCIDENT` varchar(4) DEFAULT NULL,
  `OR_CHANNEL` varchar(4) DEFAULT NULL,
  `OR_CHARGE` int(11) DEFAULT NULL,
  `OR_CHARGE_TYPE` varchar(4) DEFAULT NULL,
  `OR_CLAIM` varchar(1) DEFAULT NULL,
  `OR_CLAIM_DT` datetime DEFAULT NULL,
  `OR_DELIVERY` int(11) DEFAULT NULL,
  `OR_DISCOUNT` int(11) DEFAULT NULL,
  `OR_FEEDBACK_EMP` int(11) DEFAULT NULL,
  `OR_FEEDBACK_MEMO` varchar(100) DEFAULT NULL,
  `OR_FEEDBACK_SAT` int(11) DEFAULT NULL,
  `OR_FEEDBACK_SVR` int(11) DEFAULT NULL,
  `OR_MEMO` varchar(4000) DEFAULT NULL,
  `OR_PRICE` int(11) DEFAULT NULL,
  `OR_REFUND` varchar(4) DEFAULT NULL,
  `REGI_DT` datetime NOT NULL,
  `USER` varchar(20) NOT NULL,
  PRIMARY KEY (`OR_CD`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TbOrderVO`
--

LOCK TABLES `TbOrderVO` WRITE;
/*!40000 ALTER TABLE `TbOrderVO` DISABLE KEYS */;
/*!40000 ALTER TABLE `TbOrderVO` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-18 11:17:44
