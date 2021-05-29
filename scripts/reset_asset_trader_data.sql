--
-- Script was created by:
-- Script date:

-- #### Always check the USE <database> name value ####

-- Last modified:
-- Notes: Use this script to reset database table to a known state for testing.
--


--
-- Disable foreign keys
--
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;

--
-- Set SQL mode
--
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

--
-- Set character set the client will use to send SQL statements to the server
--
SET NAMES 'utf8';

--
-- Set default database
--
USE asset_trader1 -- change name if name to match <database> you are inserting data into!


--
-- Remove data from table asset
--
DELETE FROM asset;

--
-- Remove data from table account_type
--
DELETE FROM account_type;

--
-- Remove data from table org_unit
--
DELETE FROM org_unit;

--
-- Remove data from table user
--

DELETE FROM user;

--
-- Remove data from table trade_current
--
DELETE FROM trade_current;

--
-- Remove data from table asset_holding
--
DELETE FROM asset_holding;

--
-- Remove data from table trade_history
--
DELETE FROM trade_history;

--
-- End of table data delete
--

--
-- Populate data for table account_type
--
INSERT INTO account_type VALUES
-- account_type_id, account_type
(1, 'admin'),
(2, 'user');

--
-- Populate data for table org_unit
--
INSERT INTO org_unit VALUES
-- org_unit_id, org_unit_name, credits
(1, 'Computer Cluster Division', 1100),
(2, 'Software Access Management', 750),
(3, 'Application Design Management', 500);

--
-- Populate data for table asset
--
INSERT INTO asset VALUES
-- asset_id, asset_name
(1, 'CPU Hours'),
(2, 'MS Office 365 Subscription'),
(3, 'Adobe Acrobat Licence'),
(4, 'C++ Design Advice'),
(5, 'Hardware Troubleshooting');

--
-- Populate data for table user
--
INSERT INTO user VALUES
-- user_id, username, password, account_type_id, org_unit_id
(1, 'stock.c', '9003d1df22eb4d3820015070385194c8', 1, 1),
(2, 'jones.r', '9003d1df22eb4d3820015070385194c8', 2, 1),
(3, 'brown.a', '9003d1df22eb4d3820015070385194c8', 1, 2),
(4, 'white.v', '9003d1df22eb4d3820015070385194c8', 1, 3),
(5, 'lee.t', '9003d1df22eb4d3820015070385194c8', 2, 3);

--
-- Populate data for table trade_history
-- Commented out as this will be populated once the reconcile procedure 'sp_process_trades is run
--
-- INSERT INTO trade_history VALUES
-- trade_id_sell, trade_id_buy, trade_type, org_unit_name, username, asset_name, quantity, price, trade_date, date_processed
-- (4,	1, 'BUY', 'Software Access Management',	'jones.k', 'CPU Hours', 15, 10,	'2021-04-10 14:16:37', '2021-04-12 10:36:32'),
-- (4,	2, 'BUY', 'Software Access Management',	'jones.k', 'CPU Hours', 8, 15,	'2021-04-10 14:18:37', '2021-04-12 10:38:32'),
-- (4,	3, 'BUY', 'Software Access Management',	'jones.k', 'CPU Hours', 2, 20,	'2021-04-10 14:20:37', '2021-04-12 10:38:35'),
-- (4,	4, 'SELL', 'Application Design Management',	'stock.c', 'CPU Hours', 25, 15,	'2021-04-12 10:35:32', '2021-04-12 10:38:30');

--
-- Populate data for table trade_current
--
INSERT INTO trade_current VALUES
-- trade_id, trade_type, org_unit_id, org_unit_name, user_id, username, asset_id, asset_name, quantity, price, trade_date
(1, 'BUY', 2, 'Software Access Management', 2, 'jones.k', 1, 'CPU Hours', 15, 10, '2021-04-24 16:16:33'),
(2, 'BUY', 2, 'Software Access Management', 2, 'jones.k', 1, 'CPU Hours', 8, 15, '2021-04-24 16:18:30'),
(3, 'BUY', 2, 'Software Access Management', 2, 'jones.k', 1, 'CPU Hours', 2, 20, '2021-04-24 16:20:34'),
(4, 'SELL', 1, 'Computer Cluster Division', 1, 'stock.c', 1, 'CPU Hours', 30, 10, '2021-04-26 16:35:32'),
(5, 'SELL', 3, 'Application Design Management', 4, 'white.v', 4, 'C++ Design Advice', 5, 100, '2021-04-26 17:30:21');

--
-- Populate data for table asset_holding
--
INSERT INTO asset_holding VALUES
-- org_unit_id, asset_id, quantity
(1, 1, 50),
(1, 2, 20),
(2, 2, 30),
(2, 3, 50),
(3, 3, 5),
(3, 4, 75);

--
-- Restore previous SQL mode
--
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;

--
-- Enable foreign keys
--
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;