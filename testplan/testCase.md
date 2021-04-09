# Definition
Valid usename ( https://software.intel.com/sites/manageability/AMT_Implementation_and_Reference_Guide/default.htm?turl=WordDocuments%2Fvalidusernamesandpasswords.htm )
- A username must contain 7-bit ASCII characters, in the range of 33-126, excluding ‘:’, ‘,’, ‘<’, ‘>’, ‘&’, and ‘”’ characters. The string length is limited to 16 characters. It cannot be an empty string. The strings “Administrator” (Release3.2 only) and “Admin,” (Release 4.0 and later releases) and strings that start with “$$” are invalid.
- Passwords must contain 7-bit ASCII characters, in the range of 32-126, excluding ‘:’, ‘,’ and ‘”’ characters. 
- Price, quantity must be interger ( from  -2,147,483,648 to 2,147,483,647) and greater than 0
- Valid email: name@example.com
Case senstivity 
# USER METHODS:
### `createUser(username, pw, confirm pw, email, organisation)`
### Regular Test cases:
- Test if user enter username: kate, pw: 123, confirm pw: 123, email kate123@gmail.com, choose a organisation: ComputeClusterDivision  => okay
- Test if having some special character username kate@90, pw: 123.1,confirm pw: 123,email: 1@yahoo.com choose a organisation: ComputeClusterDivision => okay
- Test if usename length is maximum kkkkkkkkkkkkkkkk, pw: 1,confirm pw: 1,email kate123@gmail.com,  choose a organisation: ComputeClusterDivision => okay
- Test if usename and pw are the same kate123 =>okay
### Boundary test cases:
- Test if user leave usename/pw/confirm pw/organisation blank => exception
- Test if user enter a username/pw too long (more than 16 characters aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa) =>exception
### Exceptional test case:
- Test if user enter invalid usename: `kate:&/`, pw: `123`=> exception
- Test if pw does not match with confirm pw => exception
- Test if username already exist (username: kate)=> exception
- Test if unvalid email kate.12 => exception


### `login(username, password)`  
### Regular Test cases:
- Test if user enter an exist usename: kate and an exist password: 123 => okay
### Boundary test cases:
- Test if user leave usename textbox as blank =>exception
- Test if user leave pw textbox as blank =>exception
### Exceptional test case:
- Test if user enter username incorrect kate1 =>exception
- Test if user enter pw incorrect 1234 =>exception


### `logout(useName)`
### Regular Test cases:
- Test if user login, user could log out
### Exceptional test case:
- Test if not login user, fail to log out =>exception

### `resetPassword(new pw, confirm pw)`
### Regular Test cases:
- Test if user logged in, user enter a new pw: 1 and confirm pw: 1 =>okay
### Boundary test cases:
- Test if logged in, user leave pw/confirm pw empty =>exception
- Test if logged in, user enter a pw too too long (more than 16 characters aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa) =>exception
### Exceptional test case:
- Test if logged in, user enter invalid pw: 1:::::=> exception
- Test if logged in, user enter old pw: 123 => exception
- Test if logged in, pw: 1 does not match with confirm pw: 1.1 => exception


### `removeUser(username)`
### `createBuy (asset, organisation,  quantity, price)`
### Regular Test cases:
- Test if user, choose asset: CPU Hours, choose a  organisation: ComputeClusterDivision, quantity(50), price(10) => okay
- Test if user, choose asset: CPU Hours, choose a  organisation: ComputeClusterDivision, quantity(1), price(1) => okay
### Boundary test cases:
- Test if user, enter price/quantity: 0  =>exception
- Test if user, enter price/quantity too big: 2,147,483,648 =>exception 

### Exceptional test case:
- Test if user,enter price/quantity is negative number -10  =>exception
### `createSell (asset, organisation,  quantity, price)`
### Regular Test cases:
- Test if user, choose asset: CPU Hours, choose a  organisation: ComputeClusterDivision, quantity(50), price(10) => okay
- Test if user, choose asset: CPU Hours, choose a  organisation: ComputeClusterDivision, quantity(1), price(1) => okay
- Test if user, choose asset: CPU Hours, choose a  organisation: ComputeClusterDivision, quantity(2,147,483,647), price(2,147,483,647) => okay
### Boundary test cases:
- Test if user, enter price/quantity: 0  =>exception
- Test if user, enter price/quantity too big: 2,147,483,648 =>exception
- Test if user, enter quantity (60) more than number of the asset in unit (50)  =>exception
### Exceptional test case:
- Test if user,enter price/quantity is negative number -10  =>exception



# ADMIN METHODS: 
### `createUnit(organisation, totalAsset, totalCredit)`
### Regular Test cases:
- Test if admin, enter a valid organisation name: HR Manegement, total asset : 3, totalCredit: 1500 => okay
- Test if admin, enter some special character in organisation: HR@management, total asset : 3, totalCredit: 1500 => okay
- Test if admin, enter string length is maximum kkkkkkkkkkkkkkkk, total asset: 2,147,483,647, totalCredit: 2,147,483,647 => okay
- Test if admin, enter organisation: HR Management, total asset : 1, totalCredit: 2,147,483,647 => okay
- Test if admin, enter organisation: H, total asset : 2,147,483,647, totalCredit: 1 => okay
### Boundary test cases:
- Test if admin,leave organisation/totalAsset/totalCredit blank => exception
- Test if admin, enter organisation: HR@management, total asset : 0, totalCredit: 1500 =>exception
- Test if admin, enter organisation: HR@management, total asset : 10, totalCredit: 0 =>exception
- Test if admin, enter a organisation name too long (more than 16 characters aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa) =>exception
- Test if admin, enter totalAsset/totalCredit too big: 2,147,483,648 =>exceptionR Managementxception
### `createAssetType(assetType, price, quantity, organisation)`
### Regular Test cases:
- Test if admin, enter a valid asset name:CPU hours,  price(10), quantity(500) and choose a organisation: ComputeClusterDivision  => okay
- Test if admin, enter maximum length string (16 characters) for asset name kkkkkkkkkkkkkkkk => okay
- Test if admin, enter maximum price/quantity: 2,147,483,647 credits => okay
- Test if admin, assetname and price are the same (500) => okay
- Test if admin, asset name:-10, price 10 => okay
### Boundary test cases:
- Test if admin, enter the asset name  too long (more than 16 characters aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa) =>exception
- Test if admin, enter the asset name: a => okay
- Test if admin, enter price/quantity too big: 2,147,483,648 =>exception
- Test if admin, enter price/quantity: 0  =>exception
### Exceptional test case:
- Test if admin, enter invalid asset name: CPU>hours
- Test if admin, enter price/quantity is negative number -10 =>exception

### `updateAsset(asset, price, organisation)`:
- Test similar above
### `removeAsset(assetName)`
### `addAdmin(username)`
### `removeAdmin(username)`
### `removeUser(username)`
### `isAdmin(usename)`




