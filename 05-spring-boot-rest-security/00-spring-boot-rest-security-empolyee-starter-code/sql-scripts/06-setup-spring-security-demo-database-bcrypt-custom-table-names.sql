USE `employee_directory`;

DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `members`;

--
-- Table structure for table `members`
--

CREATE TABLE `members` (
  `user_id` varchar(50) NOT NULL,
  `pw` char(90) NOT NULL,
  `active` tinyint NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `members`
--
-- NOTE: The passwords are encrypted using BCrypt
--
-- A generation tool is avail at: https://www.luv2code.com/generate-bcrypt-password
--
-- Default passwords here are: name123
--

INSERT INTO `members`
VALUES
('sat','{bcrypt}$2a$10$uq.HFDiUxENbkm9Kk8l86OMkJkVnpcrMGdbYGeEQfH.0cp7vnU/LK',1),
('yog','{bcrypt}$2a$10$SYNAD002mqRRfMhrbBe4LuKlXd6SMc6vR6Zx1it6y.p1pmI9ySnYC',1),
('adm','{bcrypt}$2a$10$T4/StqBpfsTDeNAxRCBZveRNFh6EV/kN/aQEpaIzGtmCGXSy1HiPa',1);


--
-- Table structure for table `authorities`
--

CREATE TABLE `roles` (
  `user_id` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  UNIQUE KEY `authorities5_idx_1` (`user_id`,`role`),
  CONSTRAINT `authorities5_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `members` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `roles`
--

INSERT INTO `roles`
VALUES
('sat','ROLE_EMPLOYEE'),
('yog','ROLE_EMPLOYEE'),
('yog','ROLE_MANAGER'),
('adm','ROLE_EMPLOYEE'),
('adm','ROLE_MANAGER'),
('adm','ROLE_ADMIN');
