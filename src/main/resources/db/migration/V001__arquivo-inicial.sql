-- MySQL dump 10.13  Distrib 8.0.28, for Linux (x86_64)
--
-- Host: localhost    Database: wppatende
-- ------------------------------------------------------
-- Server version	8.0.28-0ubuntu0.20.04.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat` (
  `idchat` bigint NOT NULL AUTO_INCREMENT,
  `body` longtext,
  `data_tx_rx` datetime DEFAULT NULL,
  `legenda` varchar(255) DEFAULT NULL,
  `mimetype` varchar(255) DEFAULT NULL,
  `protocolo` bigint DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `tx_rx` int DEFAULT NULL,
  PRIMARY KEY (`idchat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `departamento`
--

DROP TABLE IF EXISTS `departamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departamento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `empresa_contato`
--

DROP TABLE IF EXISTS `empresa_contato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empresa_contato` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pessoaf` bigint DEFAULT NULL,
  `pessoaj` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estadoatendimento`
--

DROP TABLE IF EXISTS `estadoatendimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadoatendimento` (
  `id` int NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `mensagem` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estadoatendimento_direcionamentos`
--

DROP TABLE IF EXISTS `estadoatendimento_direcionamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadoatendimento_direcionamentos` (
  `estadoatendimento_id` int NOT NULL,
  `direcionamentos_id` int NOT NULL,
  UNIQUE KEY `UK_2tn3qipel6i0c20brlqtf97s3` (`direcionamentos_id`),
  KEY `FKp8jx7h9bqtk8c2oqj3d29meaq` (`estadoatendimento_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estadoatendimentodirecionamento`
--

DROP TABLE IF EXISTS `estadoatendimentodirecionamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadoatendimentodirecionamento` (
  `id` int NOT NULL,
  `confirmacao` bit(1) DEFAULT NULL,
  `id_proximo_estado` int DEFAULT NULL,
  `id_proximo_estado_erro` int DEFAULT NULL,
  `resposta` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `filatendimento`
--

DROP TABLE IF EXISTS `filatendimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filatendimento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_fila` datetime DEFAULT NULL,
  `protocolo_id` bigint DEFAULT NULL,
  `departamento_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrbbr7kabtvkoouwqg5xuohjah` (`departamento_id`),
  KEY `FK376hqt7cm660hrrih5x2are4m` (`protocolo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `finalizacao`
--

DROP TABLE IF EXISTS `finalizacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `finalizacao` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `flow`
--

DROP TABLE IF EXISTS `flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flow` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `initial_node_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdgkrcv48qhtefwxb4ya2h7wf1` (`initial_node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `flowinstance`
--

DROP TABLE IF EXISTS `flowinstance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flowinstance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `finish_date` datetime DEFAULT NULL,
  `initial_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `actual_node_id` bigint DEFAULT NULL,
  `flow_id` bigint DEFAULT NULL,
  `protocolo_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9ani85ck0dbluqfliqtja8v3u` (`actual_node_id`),
  KEY `FK8ky86nlqyfrdutc78y57vbnou` (`flow_id`),
  KEY `FKouy07aqstmfkqxt9u3a6rdhw` (`protocolo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `flowinstanceparameter`
--

DROP TABLE IF EXISTS `flowinstanceparameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flowinstanceparameter` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `value` longblob,
  `flow_instance_id` bigint DEFAULT NULL,
  `parameter_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmi04a09ckaieo7cc02xkvhuso` (`flow_instance_id`),
  KEY `FKa5mpee3s63xqjdhmbebjw99xr` (`parameter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `flowinstancephonenumber`
--

DROP TABLE IF EXISTS `flowinstancephonenumber`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flowinstancephonenumber` (
  `phone_number` varchar(30) NOT NULL,
  `flow_instance_id` bigint DEFAULT NULL,
  PRIMARY KEY (`phone_number`),
  KEY `FK247kgta25d9blc4elecd8qqes` (`flow_instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `flownode`
--

DROP TABLE IF EXISTS `flownode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flownode` (
  `dtype` varchar(31) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `copy_param_to_next_node` bit(1) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `end` bit(1) NOT NULL,
  `init` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `action_class` varchar(255) DEFAULT NULL,
  `collector_class` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `decision_class` varchar(255) DEFAULT NULL,
  `message_on_unrecognized_option` varchar(255) DEFAULT NULL,
  `flow_id` bigint DEFAULT NULL,
  `on_error_node_id` bigint DEFAULT NULL,
  `on_success_node_id` bigint DEFAULT NULL,
  `next_node_id` bigint DEFAULT NULL,
  `on_false_node_id` bigint DEFAULT NULL,
  `on_true_node_id` bigint DEFAULT NULL,
  `departamento_id` bigint DEFAULT NULL,
  `unrecognized_option_node_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmmnaa91fogeuetuh267r0wj2u` (`flow_id`),
  KEY `FKmaq5g1x1qpovsae5f4txfqxxm` (`on_error_node_id`),
  KEY `FKd753lnw3ce5trk0p15koxdpiw` (`on_success_node_id`),
  KEY `FK5njy6336q2n82ij5lrk33hovy` (`next_node_id`),
  KEY `FK3jgp53rc7yobd63ju5llkg3yc` (`on_false_node_id`),
  KEY `FKs4on88dttyjrmbcqu45opl0mc` (`on_true_node_id`),
  KEY `FKfocsfgxx10a0lb78ksmh749v4` (`departamento_id`),
  KEY `FK62ul9pcydnri7uxy710kdp2dt` (`unrecognized_option_node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `flownodemenuoption`
--

DROP TABLE IF EXISTS `flownodemenuoption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flownodemenuoption` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pattern` varchar(255) DEFAULT NULL,
  `menu_node_id` bigint DEFAULT NULL,
  `next_node_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqkgvljeto2580x8w1fpiwajuk` (`menu_node_id`),
  KEY `FK847ux01vmjti71v8funkeskp0` (`next_node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `flownodeparameter`
--

DROP TABLE IF EXISTS `flownodeparameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flownodeparameter` (
  `id` bigint NOT NULL,
  `deleted` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;





--
-- Table structure for table `flowparameter`
--

DROP TABLE IF EXISTS `flowparameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flowparameter` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `class_type` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` longblob,
  `flow_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiaqavu3rl64uwy6wbyk2y4mnf` (`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;




--
-- Table structure for table `parametro`
--

DROP TABLE IF EXISTS `parametro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parametro` (
  `chave` varchar(30) NOT NULL,
  `valor` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`chave`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protocolo`
--

DROP TABLE IF EXISTS `protocolo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `protocolo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cod_pessoa` bigint DEFAULT NULL,
  `cod_pessoa_juridica` bigint DEFAULT NULL,
  `data_alteracao` datetime DEFAULT NULL,
  `data_fechamento` datetime DEFAULT NULL,
  `data_inicio` datetime DEFAULT NULL,
  `finalizacao` bigint DEFAULT NULL,
  `fone` varchar(255) DEFAULT NULL,
  `operador` bigint DEFAULT NULL,
  `protocolo` varchar(255) DEFAULT NULL,
  `ult_msg_dig` text,
  `estado_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4qhrov9kt54fk9g1s00qv9v1h` (`estado_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `roteirizador`
--

DROP TABLE IF EXISTS `roteirizador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roteirizador` (
  `user_id` bigint NOT NULL,
  `data` datetime DEFAULT NULL,
  `disponivel` bit(1) NOT NULL,
  `em_atendimento` bit(1) NOT NULL,
  `nro_atendimentos` int DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbpermission`
--

DROP TABLE IF EXISTS `tbpermission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbpermission` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbpermission_roles`
--

DROP TABLE IF EXISTS `tbpermission_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbpermission_roles` (
  `tbpermission_id` bigint NOT NULL,
  `roles_id` bigint NOT NULL,
  KEY `FKrjetmpy9xojyw898sauigqjk3` (`roles_id`),
  KEY `FKdws6qxnihws39ue549cjwj706` (`tbpermission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbrole`
--

DROP TABLE IF EXISTS `tbrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbrole` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbrole_permissions`
--

DROP TABLE IF EXISTS `tbrole_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbrole_permissions` (
  `tbrole_id` bigint NOT NULL,
  `permissions_id` bigint NOT NULL,
  KEY `FK6jjhmasd6cq0dw1gh1npp377y` (`permissions_id`),
  KEY `FK8mkx0us35goc68mnr2abkei39` (`tbrole_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbuser`
--

DROP TABLE IF EXISTS `tbuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbuser` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbuser_departamentos`
--

DROP TABLE IF EXISTS `tbuser_departamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbuser_departamentos` (
  `tbuser_id` bigint NOT NULL,
  `departamentos_id` bigint NOT NULL,
  KEY `FKpm4xaohgjoo59tgoab7k8gapq` (`departamentos_id`),
  KEY `FKrws444ge1tpi6fijbv5atytku` (`tbuser_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbuser_roles`
--

DROP TABLE IF EXISTS `tbuser_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbuser_roles` (
  `users_id` bigint NOT NULL,
  `roles_id` bigint NOT NULL,
  KEY `FKppjxjfgk6rt6jlisuvu3ieshh` (`roles_id`),
  KEY `FKo0k66hgo5dnd7ex74ci3mjxe8` (`users_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-03-04 11:40:15