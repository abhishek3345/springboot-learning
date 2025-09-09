USE `employee_directory`;

DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `users`;

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` char(90) NOT NULL,
  `enabled` tinyint NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `users`
--
-- NOTE: The passwords are encrypted using BCrypt
--
-- A generation tool is avail at: https://www.luv2code.com/generate-bcrypt-password
--
-- default password is username123
--

INSERT INTO `users` 
VALUES 
('sat','{bcrypt}$2a$10$uq.HFDiUxENbkm9Kk8l86OMkJkVnpcrMGdbYGeEQfH.0cp7vnU/LK',1),
('yog','{bcrypt}$2a$10$SYNAD002mqRRfMhrbBe4LuKlXd6SMc6vR6Zx1it6y.p1pmI9ySnYC',1),
('adm','{bcrypt}$2a$10$T4/StqBpfsTDeNAxRCBZveRNFh6EV/kN/aQEpaIzGtmCGXSy1HiPa',1);


--
-- Table structure for table `authorities`
--

CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `authorities4_idx_1` (`username`,`authority`),
  CONSTRAINT `authorities4_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `authorities`
--

INSERT INTO `authorities` 
VALUES 
('sat','ROLE_EMPLOYEE'),
('yog','ROLE_EMPLOYEE'),
('yog','ROLE_MANAGER'),
('adm','ROLE_EMPLOYEE'),
('adm','ROLE_MANAGER'),
('adm','ROLE_ADMIN');