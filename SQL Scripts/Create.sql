CREATE TABLE facility (pgmSysId VARCHAR(30) PRIMARY KEY, primaryName VARCHAR(100), naicsCode VARCHAR(10), registryId BIGINT NOT NULL,
 facAddr VARCHAR(100), cityName VARCHAR(50), stateName VARCHAR(50), postalCode VARCHAR(15), latitude VARCHAR(15), longitude VARCHAR(15),
hucCode VARCHAR(15),  fipsCode VARCHAR(20));


--Clean up Code

SELECT * INTO dbo.facilityBackup FROM facility;

CREATE TABLE facilityInfo (registryId BIGINT PRIMARY KEY, primaryName VARCHAR(100), naicsCode VARCHAR(10), 
facAddr VARCHAR(100), cityName VARCHAR(50), stateName VARCHAR(50), postalCode VARCHAR(15),
latitude VARCHAR(15), longitude VARCHAR(15),
hucCode VARCHAR(15),  fipsCode VARCHAR(20));

CREATE TABLE facilityMapping (pgmSysId VARCHAR(30) PRIMARY KEY, registryId 
BIGINT FOREIGN KEY REFERENCES facilityInfo(registryID) );

INSERT INTO facilityInfo
         SELECT registryId, MAX(primaryName), MAX(naicsCode),
		 MAX(facAddr),  MAX(cityName),
		  MAX(stateName),  MAX(postalCode), MAX(latitude),
		 MAX(longitude), MAX(hucCode),
		 MAX(fipsCode)    /* all attributes of the facility table except pgmSysId */
	FROM facility
         GROUP BY registryId;
		 
INSERT INTO facilityMapping SELECT pgmSysId, registryId FROM facility ;


-----------------------

CREATE TABLE generation (plantCode VARCHAR(30) FOREIGN KEY REFERENCES facilityMapping(pgmSysId), plantName VARCHAR(100), 
genYear INT, genMonth INT, genData FLOAT(4), units VARCHAR (20), PRIMARY KEY (plantCode, genMonth, genYear) );

ALTER TABLE generation ADD latitude VARCHAR (15), longitude VARCHAR (15);

CREATE TABLE generationRange (plantCode VARCHAR(30) FOREIGN KEY REFERENCES facilityMapping(pgmSysId), genStart INT, genEnd INT);


-----------------------
CREATE TABLE gasInfo (gasId INT PRIMARY KEY, gasCode VARCHAR(10), gasName VARCHAR(100), gasLabel VARCHAR(100) );

CREATE TABLE emissions (registryId BIGINT FOREIGN KEY REFERENCES facilityInfo(registryId), latitude VARCHAR (15), longitude VARCHAR (15), emYear INT, 
gasId INT FOREIGN KEY REFERENCES gasInfo(gasId), emissionAmount FLOAT(3), sector INT, PRIMARY KEY (registryId, emYear, gasId) ); --TODO frsId--registryId & PRIMARY KEY & sector

-----------------------


--One ratio / coeff per registryId
CREATE TABLE emissionsCoeff (registryId BIGINT FOREIGN KEY REFERENCES facilityInfo(registryId),
dataYear INT , emPerGen FLOAT(5), PRIMARY KEY(registryId, dataYear) );

--One ratio / coeff per pgmSysId
CREATE TABLE waterCoeff (pgmSysId VARCHAR(30) PRIMARY KEY FOREIGN KEY REFERENCES facilityMapping(pgmSysId), 
plantType VARCHAR(30), dataYear INT, usagePerGen FLOAT(5) );   --gallons/mwh static coefficient per plant type


--ALTER TABLE facilityInfo ADD COLUMN (plantType VARCHAR(30));

CREATE TABLE waterUsage (plantCode VARCHAR(30) , plantName VARCHAR(100), 
usageYear INT, usageMonth INT, usageData BIGINT, units VARCHAR(20), PRIMARY KEY(plantCode, usageYear, usageMonth));

ALTER TABLE waterUsage ADD derived BIT;

CREATE TABLE dynamicWaterCoeff (plantCode VARCHAR(30), usageYear INT, usagePerGen FLOAT(5), PRIMARY KEY(plantCode, usageYear));
CREATE TABLE waterCoeffStatic (plantType VARCHAR(30) PRIMARY KEY FOREIGN KEY REFERENCES facility860Info(plantType), usagePerGen FLOAT(5));

CREATE TABLE emissionsMonthly (registryId BIGINT FOREIGN KEY REFERENCES facility860Info(registryId),
emYear INT, emMonth INT, emissionAmount FLOAT(3), derived BIT,
PRIMARY KEY (registryId, emYear, emMonth) );

ALTER TABLE waterUsage DROP COLUMN plantName; --We don't need plantname here since name will always be taken from facility Info table
ALTER TABLE waterUsage ALTER COLUMN usageData FLOAT(5);