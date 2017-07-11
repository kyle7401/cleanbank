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
-- Table structure for table `TB_PART_MEMBER`
--

DROP TABLE IF EXISTS `TB_PART_MEMBER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TB_PART_MEMBER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEL_YN` varchar(1) NOT NULL COMMENT '삭제여부',
  `EVT_NM` varchar(30) DEFAULT NULL COMMENT '이벤트명',
  `PM_EMAIL` varchar(50) NOT NULL COMMENT '공장아이디',
  `PM_NM` varchar(30) NOT NULL COMMENT '담당자명',
  `PM_PASS` varchar(100) NOT NULL COMMENT '암호',
  `PM_TEL` varchar(20) NOT NULL COMMENT '전화번호',
  `PT_CD` int(11) NOT NULL COMMENT '공장코드',
  `REGI_DT` datetime NOT NULL COMMENT '작성일자',
  `USER` varchar(20) NOT NULL COMMENT '작성자',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='공장담당자정보';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TB_PART_MEMBER`
--

LOCK TABLES `TB_PART_MEMBER` WRITE;
/*!40000 ALTER TABLE `TB_PART_MEMBER` DISABLE KEYS */;
INSERT INTO `TB_PART_MEMBER` VALUES (1,'','','hyoseop@synapsetech.co.kr','송효섭','$2a$10$nW5J.eleEp.Z92/VxFBS2.HyRZkzWtEvnL0bpKTEE4z2zsXF9iTTq','010-9087-6734',1,'2015-12-01 00:00:00','anonymousUser'),(2,'','','test2@abc.com','테스트222','$2a$10$p4G8xpRlIbqG3GAGjOLBMu/xemBFpGkOcM2UD0CzSycF5LUIZeLmi','02-8834-3432',1,'2015-12-01 00:00:00','anonymousUser'),(3,'Y','','test2@abc.com','테스트2222','$2a$10$82SAwN1fPBo3XK.HAdy1me1nLH7NW5KKLCcwvWPDytDqmkDkzNK7S','02-8834-3432',1,'2015-12-01 18:31:38','anonymousUser'),(4,'','','test3@abc.com','테스트3','$2a$10$CoOr.T722ONLhY19jiTJteEfi9tfC73Allfw/4eqa3luh7CYPtWLS','02-1234-5678',1,'2015-12-01 18:34:46','anonymousUser'),(5,'','','ㅇㅁㄴdasdad','아무개','$2a$10$MwZ3E/Ap80ErUu1sASCNWe3O1F5fqJpPxk3FVeBceqSi47iJQydDa','010-7411-1111',1,'2015-12-05 15:49:30','anonymousUser'),(6,'','','test1','공장 test1','$2a$10$hoV.ihOAptullJkPW7BhcO2ndDCbXLMF.BcXAnA3S5oJiTZEKtZry','1',2,'2015-12-17 00:00:00','anonymousUser'),(7,'','','text2','test2','$2a$10$iEmqViWU8YXqk7nBEWCJyuFkQavpE31NgEkpzoIQDcU1/8th6tn76','2',2,'2016-02-11 00:00:00','(주)시냅스테크놀로지'),(8,'','','234234','123123','$2a$10$XgHBi4DfY06ZCBePz3MTeOuc43ORelDpLkjvVuHYNXCBWFOmL5YrS','123123',2,'2016-02-17 16:47:14','(주)시냅스테크놀로지'),(9,'','','공장테스트','testtest','$2a$10$L7YD2zH7qkpZ9btjH06RTuvOcVoPt2sZJz6t7F1YfxFeQW8OPkCUq','123',4,'2016-03-02 20:56:11','(주)시냅스테크놀로지'),(10,'','','factory','테스트공장','$2a$10$2G6K99GJC5C0dO6KbnnMweaHSUMOUcusMNC90c18xOpnpi52BFPly','010-7435-1031',5,'2016-03-10 16:53:15','(주)시냅스테크놀로지');
/*!40000 ALTER TABLE `TB_PART_MEMBER` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-23 11:07:08
