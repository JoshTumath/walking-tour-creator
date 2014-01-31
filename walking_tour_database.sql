-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 31, 2014 at 06:48 AM
-- Server version: 5.5.31
-- PHP Version: 5.4.4-14+deb7u5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `walking_tour_database`
--

-- --------------------------------------------------------

--
-- Table structure for table `listOfWalks`
--

CREATE TABLE IF NOT EXISTS `listOfWalks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 NOT NULL,
  `shortDesc` varchar(255) CHARACTER SET utf8 NOT NULL,
  `longDesc` varchar(1000) CHARACTER SET utf8 NOT NULL,
  `time` int(11) NOT NULL,
  `distance` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19 ;

--
-- Dumping data for table `listOfWalks`
--

INSERT INTO `listOfWalks` (`id`, `title`, `shortDesc`, `longDesc`, `time`, `distance`) VALUES
(17, 'Cold', 'damn its cold', 'another rainy day in aberystwyth, time for a walk', 222, 0),
(18, 'anotherWalk', 'physics block to compuer science block', 'another walk in the wet and windy aberystwyth', 165, 0);

-- --------------------------------------------------------

--
-- Table structure for table `location`
--

CREATE TABLE IF NOT EXISTS `location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `walkID` int(11) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `poi` tinyint(1) NOT NULL,
  `timestamp` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=30 ;

--
-- Dumping data for table `location`
--

INSERT INTO `location` (`id`, `walkID`, `latitude`, `longitude`, `poi`, `timestamp`) VALUES
(1, 17, 52.4162, -4.0661, 0, -401601),
(2, 17, 52.4162, -4.06632, 0, -401588),
(3, 17, 52.4161, -4.06648, 0, -401576),
(4, 17, 52.4161, -4.06684, 0, -401526),
(5, 17, 52.4156, -4.06679, 0, -401493),
(6, 17, 52.4156, -4.06679, 0, -401493),
(7, 17, 52.4156, -4.06679, 0, -401492),
(8, 17, 52.4155, -4.06669, 0, -401484),
(9, 17, 52.4155, -4.06669, 0, -401484),
(10, 17, 52.4155, -4.06667, 0, -401483),
(11, 17, 52.4154, -4.06648, 0, -401472),
(12, 17, 52.4154, -4.06648, 0, -401472),
(13, 17, 52.4154, -4.06646, 0, -401470),
(14, 17, 52.4159, -4.06586, 0, -401379),
(15, 17, 52.4159, -4.06586, 0, -401379),
(16, 17, 52.416, -4.06664, 1, -401557),
(17, 17, 52.4155, -4.06614, 1, -401455),
(18, 18, 52.4157, -4.06475, 0, -401160),
(19, 18, 52.4159, -4.06472, 0, -401139),
(20, 18, 52.416, -4.06484, 0, -401129),
(21, 18, 52.4161, -4.06495, 0, -401121),
(22, 18, 52.4162, -4.06505, 0, -401111),
(23, 18, 52.4162, -4.06527, 0, -401096),
(24, 18, 52.4162, -4.06549, 0, -401085),
(25, 18, 52.4163, -4.06566, 0, -401077),
(26, 18, 52.4164, -4.06576, 0, -401067),
(27, 18, 52.4163, -4.06595, 0, -401054),
(28, 18, 52.4156, -4.06551, 1, -401214),
(29, 18, 52.4163, -4.06604, 1, -401049);

-- --------------------------------------------------------

--
-- Table structure for table `photoUsage`
--

CREATE TABLE IF NOT EXISTS `photoUsage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `placeID` int(11) NOT NULL,
  `photoName` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `photoUsage`
--

INSERT INTO `photoUsage` (`id`, `placeID`, `photoName`) VALUES
(1, 1, 'd0b083c1987d41461be8164fcb5e5c65'),
(2, 2, '79a3632d7008e3ff5e92db3b813b6dd9'),
(3, 4, '4991def86f826d9162bff59d25934f46'),
(4, 4, 'b8135a4babc67646bab17557c400d2eb');

-- --------------------------------------------------------

--
-- Table structure for table `placeDescription`
--

CREATE TABLE IF NOT EXISTS `placeDescription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `locationID` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `placeDescription`
--

INSERT INTO `placeDescription` (`id`, `locationID`, `title`, `description`) VALUES
(1, 16, 'stairs', 'hiding under shelter'),
(2, 17, 'mire stairs', 'more stairs '),
(3, 28, 'phsics', 'st the physics block, man its windy'),
(4, 29, 'back home', 'back to b23');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
