--------------------------
--We can use this view to replace blank values 
CREATE VIEW facAllRowsTemp AS
SELECT DISTINCT registryId,  REPLACE(primaryName,' ', '') AS primaryName, 
REPLACE(naicsCode,' ', '') AS naicsCode, REPLACE(facAddr,' ', '') AS facAddr,
 REPLACE(cityName,' ', '') AS cityName, REPLACE(stateName,' ', '') AS stateName,
 REPLACE(postalCode,' ', '') AS postalCode,  REPLACE(latitude,' ', '') AS latitude,
  REPLACE(longitude,' ', '') AS longitude,  REPLACE(hucCode,' ', '') AS hucCode,
   REPLACE(fipsCode,' ', '') AS fipsCode
FROM facility

--Check which registryIds have more than one pgmSysId attached to them
SELECT registryId, COUNT(*)
FROM facAllRowsTemp
GROUP BY registryId
HAVING COUNT(*) > 1;

--------------------------------------
--Merging generation from EIA and FRS
SELECT COUNT(distinct plantCode) FROM generation -- 2490  only in A
SELECT COUNT(distinct plantCode) FROM generationEIA -- 6999  only in B
SELECT COUNT(distinct g2.plantCode) FROM generation g1, generationEIA g2 WHERE g1.plantCode = g2.plantCode -- 2403   - A intersection B

CREATE VIEW genTotal AS
SELECT * FROM generation union SELECT * FROM generationEIA

SELECT COUNT(distinct plantCode) FROM genTotal 
--Answer – 7086   which is also exactly equal to (6999 + 2490) – 2403 that means union was done successfully. 

SELECT genYear, SUM(genData) AS ourGenSum
FROM genTotal
WHERE genYear = 2016 or genYear = 2017
GROUP BY genYear
--------------------------------------

--Merging facilities from T_FRS_NAICS_EZ and new data
SELECT COUNT(distinct registryId) FROM facilityInfo -- 21100  only in A
SELECT COUNT(distinct registryId) FROM facilityNew -- 7074  only in B
SELECT COUNT(distinct g1.registryId) FROM facilityInfo g1, facilityNew g2 WHERE g1.registryId = g2.registryId -- 2676   - A intersection B

CREATE VIEW facilityJoin AS
SELECT * FROM facilityInfo union SELECT distinct registryId, primaryName , naicsCode ,
facAddr , cityName , stateName , postalCode , latitude , longitude ,
hucCode,  fipsCode FROM facilityNew 
--COUNT -> 25498 = 21100+7074-2676



-----------------------------
--Query to split facility data into Info and Mapping 

INSERT INTO facilityInfo
         SELECT registryId, MAX(primaryName), MAX(naicsCode),
		 MAX(facAddr),  MAX(cityName),
		  MAX(stateName),  MAX(postalCode), MAX(latitude),
		 MAX(longitude), MAX(hucCode),
		 MAX(fipsCode)    /* all attributes of the facility table except pgmSysId */
	FROM facility
         GROUP BY registryId HAVING registryId NOT IN (SELECT distinct registryId FROM facilityInfo);
		 
INSERT INTO facilityMapping SELECT pgmSysId, registryId FROM facility WHERE pgmSysId 
NOT IN (SELECT pgmSysId FROM facilityMapping);

SELECT COUNT(distinct f.pgmSysId), f.registryId FROM generation g, facilityMapping f 
WHERE g.plantCode = f.pgmSysId 
 GROUP BY f.registryId  HAVING COUNT(distinct f.pgmSysId) > 2

 ---------------------------
 
 -- EmissionsCoefficient creation -- 

--Sum up emissions for all gasIds  per registryId and year
CREATE VIEW emPerYear AS 
SELECT registryId, emYear, SUM(emissionAmount) AS emission FROM emissions GROUP BY registryId, emYear

--Sum up generations for all months per plantCode and year
CREATE VIEW genPerYear AS
SELECT plantCode, genYear, SUM(genData) AS generation FROM generation GROUP BY plantCode, genYear

--Sum up generation for all facilites under one registryId per year
CREATE VIEW genPerRegPerYear AS
SELECT registryId, genYear, SUM(generation) AS genSum FROM facilityMapping f, genPerYear g
WHERE f.pgmSysId = g.plantCode
GROUP BY registryId, genYear 

--Check which facilities have generation and emissions
SELECT * FROM emPerYear e, genPerRegPerYear g WHERE e.registryid = g.registryId AND e.emYear = g.genYear

--Creation of coefficients
INSERT INTO emissionsCoeff
SELECT e.registryId, e.emYear, (emission/genSum) AS coeff FROM emPerYear e, genPerRegPerYear g
 WHERE e.registryid = g.registryId AND e.emYear = g.genYear AND genSum !=0  ORDER BY registryId, emYear

 ---------------------------------

============================= 
--Emission creation for facility860 (new data) (same as above but with table names changed)
CREATE VIEW emPerYear AS 
SELECT registryId, emYear, SUM(emissionAmount) AS emission FROM emissions GROUP BY registryId, emYear

CREATE VIEW genPerYear AS
SELECT plantCode, genYear, SUM(genData) AS generation FROM generation GROUP BY plantCode, genYear

 
CREATE VIEW genPerRegPerYear AS
SELECT registryId, genYear, SUM(generation) AS genSum FROM facility860Mapping f, genPerYear g
WHERE f.pgmSysId = g.plantCode
GROUP BY registryId, genYear 



--This view gives us summed up generations for all registryIds
--which might have more than one plantCode with generations for each month for each year

CREATE VIEW genPerRegPerYearPerMonth AS
SELECT registryId, genYear, genMonth, SUM(genData) AS genSum FROM facility860Mapping f, generation g
WHERE f.pgmSysId = g.plantCode
GROUP BY registryId, genYear, genMonth ORDER BY registryId, genYear, genMonth 

------WATER USAGE------

INSERT INTO waterUsage 
SELECT  [plantCode], plantName
      ,[usageYear] ,[usageMonth] 
      ,SUM(ISNULL([withdrawalVolume],0) + ISNULL([consumptionVolume],0)) AS usageData, 'Million Gallons'
  FROM [EPA].[dbo].[Water 2014] GROUP BY plantCode, plantName, usageYear, usageMonth ORDER BY plantCode, usageYear, usageMonth 

  UPDATE waterUsage set derived = 0
  
CREATE VIEW waterUsagePerYear AS
SELECT plantCode, usageYear, SUM(usageData) AS waterUsage FROM waterUsage GROUP BY plantCode, usageYear  

INSERT INTO dynamicWaterCoeff 
SELECT e.plantCode, e.usageYear, (waterUsage/generation) AS usagePerGen FROM waterUsagePerYear e, genPerYear g
WHERE e.plantCode = g.plantCode AND e.usageYear = g.genYear AND generation !=0  ORDER BY e.plantCode, usageYear

EXEC fillWaterUsage @startYear=2008, @endYear=2018




