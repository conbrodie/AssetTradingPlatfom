select trade_type, org_unit_name, asset_name, sum(quantity), sum(quantity * price),
     (select credits from org_unit where org_unit_id = tc.org_unit_id),
     (select COALESCE(quantity, 0) from asset_holding where org_unit_id = tc.org_unit_id and asset_id = tc.asset_id)
from trade_current tc
where trade_type = 'SELL' and asset_name = 'CPU Hours' and org_unit_name = 'Computer Cluster Division'
group by trade_type, org_unit_id, asset_name
order by trade_type;


-- used to manually insert a row (take care with the ID)
-- INSERT INTO trade_current VALUES
-- trade_id, trade_type, org_unit_id, org_unit_name, user_id, username, asset_id, asset_name, quantity, price, trade_date
-- (15, 'SELL',	1,	'Computer ClusterX',	2,	'jones.x',	4,	'MS Office',	1,	100,	'2021-04-26 11:16:30');


--  if you get 'The user specified as a definer ('root'@'%') does not exist'
-- Log on to MySQL and execute
mysql -u root -p -- sign in with root password (on machine that has mariaDB installed
grant all on *.* to 'root'@'%' identified by 'password' with grant option; -- need change to your root password
flush privileges -- may not need this?



