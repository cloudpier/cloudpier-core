--
-- Init data for table `Usertype`
--

 INSERT INTO Usertype (id, name) VALUES (1, 'developer');
 INSERT INTO Usertype (id, name) VALUES (2, 'paasprovider');

-- 
-- Init data for the UserExperienceTest
-- We put these initializing data;
-- * 1 user ( uriId = user1UriId )
-- * 1 paas ( uriId = paas1UriId )
-- * 1 account ( user1 on paas1 )
-- * 2 application instances ( app1UriID and app2UriID, both linked to the same account)
-- * 1 userExperienceRate ( linked to app2, rate value 4)
--

 INSERT INTO C4sUser (id, usertype, fullname, username, password, c4spublickey, uriID) VALUES (1, 1, 'User1', 'user1', 'pass1', 'c4sPublicKey', 'user1UriId'); 

 INSERT INTO Paas (id, name, url, info, uriID) VALUES (1, 'paasExample', 'www.paasexample.com', 'Some infos', 'paas1UriID' );
 INSERT INTO Paas (id, name, url, info, uriID) VALUES (2, 'paas2Example', 'www.paas2example.com', 'Some infos', 'paas2UriID' ); 

 INSERT INTO Account (id, paas, c4suser, accountname, publickey, privatekey) VALUES (1, 1, 1, 'user1onPaas1', 'paasPublicKey', 'paasPrivateKey' ); 

 INSERT INTO Status (id, name) VALUES (1, 'testStatus' ); 

 INSERT INTO Applicationinstance (id, appname, appurl, adapterurl, version, account, status, uriID) VALUES (1, 'app1test', 'www.app1test.com', 'http://app1AdapterUrl.com', '1.0', 1, 1, 'app1UriID' ); 
 INSERT INTO Applicationinstance (id, appname, appurl, adapterurl, version, account, status, uriID) VALUES (2, 'app2test', 'www.app2test.com', 'http://app2AdapterUrl.com', '1.0', 1, 1, 'app2UriID' ); 
 INSERT INTO Applicationinstance (id, appname, appurl, adapterurl, version, account, status, uriID) VALUES (3, 'app3test', 'www.app3test.com', 'http://app3AdapterUrl.com', '1.0', 1, 1, 'app3UriID' );
 INSERT INTO Applicationinstance (id, appname, appurl, adapterurl, version, account, status, uriID) VALUES (4, 'app4test', 'www.app3test.com', 'http://app3AdapterUrl.com', '1.0', 1, 1, 'app4UriID' ); 

 INSERT INTO UserExperienceRate ( id, appId, rate) VALUES ( 1, 2, 4);
 INSERT INTO UserExperienceRate ( id, appId, rate) VALUES ( 2, 3, 2);
 INSERT INTO UserExperienceRate ( id, appId, rate) VALUES ( 3, 4, 3);
