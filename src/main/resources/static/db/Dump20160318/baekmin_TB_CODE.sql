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
-- Table structure for table `TB_CODE`
--

DROP TABLE IF EXISTS `TB_CODE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TB_CODE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CD_GP` varchar(2) NOT NULL COMMENT '상위코드',
  `CD_IT` varchar(2) NOT NULL COMMENT '하위코드',
  `CD_NM` varchar(20) NOT NULL COMMENT '코드명',
  `CD_SORT` int(11) DEFAULT NULL COMMENT '정렬순서',
  `DEL_YN` varchar(1) DEFAULT NULL COMMENT '삭제여부',
  `EVT_NM` varchar(30) DEFAULT NULL COMMENT '이벤트명',
  `REGI_DT` datetime NOT NULL COMMENT '작성일자',
  `USER` varchar(20) NOT NULL COMMENT '작성자',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8 COMMENT='코드정보';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_CODE`
--

LOCK TABLES `TB_CODE` WRITE;
/*!40000 ALTER TABLE `TB_CODE` DISABLE KEYS */;
INSERT INTO `TB_CODE` VALUES (1,'01','01','주문접수',1,'','','2015-11-18 00:00:00','anonymousUser'),(2,'01','00','주문상태',0,'','','2015-11-19 00:00:00','anonymousUser'),(3,'01','02','수거 중',2,'','','2015-11-26 00:00:00','anonymousUser'),(4,'01','03','수거완료',3,NULL,NULL,'2015-11-27 13:06:41','hyoseop'),(5,'01','04','검수완료',4,'','','2015-11-27 00:00:00','hyoseop'),(6,'01','05','공장입고',5,NULL,NULL,'2015-11-27 13:07:10','hyoseop'),(7,'01','06','세탁중',6,NULL,NULL,'2015-11-27 13:07:10','hyoseop'),(8,'01','07','공장출고',7,NULL,NULL,'2015-11-27 13:07:10','hyoseop'),(9,'01','08','백민입고',8,NULL,NULL,'2015-11-27 13:07:10','hyoseop'),(10,'01','09','배송중',9,NULL,NULL,'2015-11-27 13:07:10','hyoseop'),(11,'01','10','배송완료',10,NULL,NULL,'2015-11-27 13:07:10','hyoseop'),(12,'02','00','클레임상태',0,NULL,NULL,'2015-11-27 13:11:36','hyoseop'),(13,'02','01','클레임요청',1,NULL,NULL,'2015-11-27 13:11:36','hyoseop'),(14,'02','02','클레임수거중',2,NULL,NULL,'2015-11-27 13:11:36','hyoseop'),(15,'02','03','클레임수거완료',3,NULL,NULL,'2015-11-27 13:11:36','hyoseop'),(16,'02','04','클레임접수완료',4,NULL,NULL,'2015-11-27 13:11:36','hyoseop'),(17,'02','05','클레임공장입고',5,NULL,NULL,'2015-11-27 13:11:37','hyoseop'),(18,'02','06','클레임세탁중',6,NULL,NULL,'2015-11-27 13:11:37','hyoseop'),(19,'02','07','클레임공장출고',7,NULL,NULL,'2015-11-27 13:11:37','hyoseop'),(20,'02','08','클레임백민입고',8,NULL,NULL,'2015-11-27 13:11:37','hyoseop'),(21,'02','09','클레임배송중',9,NULL,NULL,'2015-11-27 13:11:37','hyoseop'),(22,'02','10','클레임배송완료',10,NULL,NULL,'2015-11-27 13:11:37','hyoseop'),(23,'03','00','환불상태',0,NULL,NULL,'2015-11-27 13:12:55','hyoseop'),(24,'03','01','부분환불',1,NULL,NULL,'2015-11-27 13:12:55','hyoseop'),(25,'03','02','환불상태',2,NULL,NULL,'2015-11-27 13:12:55','hyoseop'),(26,'03','03','보상',3,NULL,NULL,'2015-11-27 13:12:55','hyoseop'),(27,'04','00','사고상태',0,NULL,NULL,'2015-11-27 13:14:47','hyoseop'),(28,'04','01','백민사고',1,NULL,NULL,'2015-11-27 13:14:47','hyoseop'),(29,'04','02','고객사고',2,NULL,NULL,'2015-11-27 13:14:47','hyoseop'),(30,'04','03','공장사고',3,NULL,NULL,'2015-11-27 13:14:47','hyoseop'),(31,'04','04','공동사고',4,NULL,NULL,'2015-11-27 13:14:47','hyoseop'),(32,'05','00','결제상태',0,NULL,NULL,'2015-11-27 13:16:08','hyoseop'),(33,'05','01','결제대기',1,NULL,NULL,'2015-11-27 13:16:08','hyoseop'),(34,'05','02','결제완료',2,NULL,NULL,'2015-11-27 13:16:08','hyoseop'),(35,'05','03','결제취소',3,NULL,NULL,'2015-11-27 13:16:09','hyoseop'),(36,'06','00','서비스상태',0,NULL,NULL,'2015-11-27 15:43:58','hyoseop'),(37,'06','01','정상',1,NULL,NULL,'2015-11-27 15:43:58','hyoseop'),(38,'06','02','제한',2,NULL,NULL,'2015-11-27 15:43:58','hyoseop'),(39,'07','00','담당업무',0,NULL,NULL,'2015-11-30 15:05:49','hyoseop'),(40,'07','01','배송',1,NULL,NULL,'2015-11-30 15:05:49','hyoseop'),(41,'07','02','수거',2,NULL,NULL,'2015-11-30 15:05:49','hyoseop'),(42,'08','00','코디등급',0,NULL,NULL,'2015-11-30 15:05:49','hyoseop'),(43,'08','01','일반',1,NULL,NULL,'2015-11-30 15:05:49','hyoseop'),(44,'09','00','운전능력',0,NULL,NULL,'2015-11-30 15:14:37','hyoseop'),(45,'09','01','일반차량',1,NULL,NULL,'2015-11-30 15:14:37','hyoseop'),(46,'09','02','특수차량',2,NULL,NULL,'2015-11-30 15:14:37','hyoseop'),(47,'09','03','오토바이',3,NULL,NULL,'2015-11-30 15:14:37','hyoseop'),(48,'10','00','유입경로',0,'','','2015-12-28 13:39:54','(주)시냅스테크놀로지'),(49,'10','01','카카오톡',1,'','','2015-12-28 13:40:14','(주)시냅스테크놀로지'),(50,'10','02','네이버',2,'','','2015-12-28 13:40:27','(주)시냅스테크놀로지'),(51,'10','03','페이스북',3,'','','2015-12-28 13:40:40','(주)시냅스테크놀로지'),(52,'01','11','주문취소',11,'','','2015-12-28 16:31:29','(주)시냅스테크놀로지'),(53,'10','04','전화',4,'','','2015-12-28 16:45:41','(주)시냅스테크놀로지'),(54,'10','05','방문',5,'','','2015-12-28 16:46:13','(주)시냅스테크놀로지'),(55,'02','11','클레임취소',11,'','','2015-12-28 00:00:00','(주)시냅스테크놀로지');
/*!40000 ALTER TABLE `TB_CODE` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-18 12:55:07
