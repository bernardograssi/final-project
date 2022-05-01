-- Get all data from the database.
DROP PROCEDURE IF EXISTS getAllData;

DELIMITER $$
CREATE PROCEDURE getAllData()
BEGIN
	SELECT N.id, N.name, R.band_max, R.band_min, C.mag_curve, C.lum_curve, D.delta_value, CA.calc_res_1, CA.calc_res_2, B.magnitude, B.luminosity, T.time_value
	FROM sn_names N
	JOIN sn_results R ON N.id = R.sn_id
	JOIN sn_curves C ON R.curve_id = C.id
	JOIN sn_deltas D ON R.band_delta_id = D.id
	JOIN sn_calc CA ON R.calc_res_id = CA.id
	JOIN sn_band_association B ON R.sn_id = B.sn_id
	JOIN sn_time_association T ON B.id = T.id;
END $$
DELIMITER ;

-- Get all data of a specific supernova.
DROP PROCEDURE IF EXISTS getDataByName;

DELIMITER $$
CREATE PROCEDURE getDataByName(IN theName VARCHAR(255))
BEGIN
	SELECT N.id, N.name, R.band_max, R.band_min, C.mag_curve, C.lum_curve, D.delta_value, CA.calc_res_1, CA.calc_res_2, B.magnitude, B.luminosity, T.time_value
	FROM sn_names N
	JOIN sn_results R ON N.id = R.sn_id
	JOIN sn_curves C ON R.curve_id = C.id
	JOIN sn_deltas D ON R.band_delta_id = D.id
	JOIN sn_calc CA ON R.calc_res_id = CA.id
	JOIN sn_band_association B ON R.sn_id = B.sn_id
	JOIN sn_time_association T ON B.id = T.id
    WHERE N.name = theName;
END $$
DELIMITER ;

-- Insert name into sn_names.
DROP PROCEDURE IF EXISTS insertName;

DELIMITER $$
CREATE PROCEDURE insertName(IN theName VARCHAR(255))
BEGIN
	INSERT INTO sn_names (name) VALUES (theName);
END $$
DELIMITER ;

-- Insert time and id into sn_time_association.
DROP PROCEDURE IF EXISTS insertTime;

DELIMITER $$
CREATE PROCEDURE insertTime(IN theTime DOUBLE, IN theId INT)
BEGIN
	INSERT INTO sn_time_association (time_value, sn_id) VALUES (theTime, theId);
END $$
DELIMITER ;

-- Insert magnitude and luminosity into sn_band_association.
DROP PROCEDURE IF EXISTS insertValues;

DELIMITER $$
CREATE PROCEDURE insertValues(IN theMagnitude DOUBLE, IN theLuminosity DOUBLE, IN theId INT)
BEGIN
	INSERT INTO sn_band_association (magnitude, luminosity, sn_id) VALUES (theMagnitude, theLuminosity, theId);
END $$
DELIMITER ;

-- Insert delta_value and sn_id into sn_deltas.
DROP PROCEDURE IF EXISTS insertDelta;

DELIMITER $$
CREATE PROCEDURE insertDelta(IN theId INT, IN theDelta DOUBLE)
BEGIN
	INSERT INTO sn_deltas (sn_id, delta_value) VALUES (theId, theDelta);
END $$
DELIMITER ;

-- Insert sn_id, mag_curve, and lum_curve into sn_curves.
DROP PROCEDURE IF EXISTS insertCurves;

DELIMITER $$
CREATE PROCEDURE insertCurves(IN theId INT, IN theMag DOUBLE, IN theLum DOUBLE)
BEGIN
	INSERT INTO sn_curves (sn_id, mag_curve, lum_curve) VALUES (theId, theMag, theLum);
END $$
DELIMITER ;

-- Insert sn_id, calc_res_1, and calc_res_2 into sn_calc.
DROP PROCEDURE IF EXISTS insertCalc;

DELIMITER $$
CREATE PROCEDURE insertCalc(IN theId INT, IN resOne DOUBLE, IN resTwo DOUBLE)
BEGIN
	INSERT INTO sn_calc (sn_id, calc_res_1, calc_res_2) VALUES (theId, resOne, resTwo);
END $$
DELIMITER ;

-- Insert sn_id, calc_res_1, and calc_res_2 into sn_calc.
DROP PROCEDURE IF EXISTS insertResults;

DELIMITER $$
CREATE PROCEDURE insertResults(IN theId INT, IN theBandMax DOUBLE, IN theBandMin DOUBLE, IN theBandDeltaId INT, IN theCurveId INT, IN theCalcResId INT)
BEGIN
	INSERT INTO sn_results (sn_id, band_max, band_min, band_delta_id, curve_id, calc_res_id) VALUES (theId, theBandMax, theBandMin, theBandDeltaId, theCurveId, theCalcResId);
END $$
DELIMITER ;

-- Get id by name from sn_names.
DROP PROCEDURE IF EXISTS getIdByName;

DELIMITER $$
CREATE PROCEDURE getIdByName(IN theName VARCHAR(255))
BEGIN
	SELECT Id FROM sn_names S WHERE S.Name = theName;
END $$
DELIMITER ;

-- Get last inserted id in sn_deltas.
DROP PROCEDURE IF EXISTS getLastBandId;

DELIMITER $$
CREATE PROCEDURE getLastBandId()
BEGIN
	SELECT MAX(id) AS "maxId" FROM sn_deltas;
END $$
DELIMITER ;

-- Get last inserted id in sn_curves.
DROP PROCEDURE IF EXISTS getLastCurveId;

DELIMITER $$
CREATE PROCEDURE getLastCurveId()
BEGIN
	SELECT MAX(id) AS "maxId" FROM sn_curves;
END $$
DELIMITER ;

-- Get last inserted id in sn_calc.
DROP PROCEDURE IF EXISTS getLastCalcId;

DELIMITER $$
CREATE PROCEDURE getLastCalcId()
BEGIN
    SELECT MAX(id) AS "maxId" FROM sn_calc;
END $$
DELIMITER ;

-- Delete all data related to a specific id.
DROP PROCEDURE IF EXISTS deleteById;

DELIMITER $$
CREATE PROCEDURE deleteById(IN theId INT)
BEGIN
    DELETE FROM sn_band_association WHERE sn_id = theId;
	DELETE FROM sn_time_association WHERE sn_id = theId;
	DELETE FROM sn_results WHERE sn_id = theId;
	DELETE FROM sn_calc WHERE sn_id = theId;
	DELETE FROM sn_curves WHERE sn_id = theId;
	DELETE FROM sn_deltas WHERE sn_id = theId;
	DELETE FROM sn_names WHERE id = theId;
END $$
DELIMITER ;

-- Reset the database.
DROP PROCEDURE IF EXISTS resetDatabase;

DELIMITER $$
CREATE PROCEDURE resetDatabase()
BEGIN
    DELETE FROM sn_band_association WHERE id > 0;
	DELETE FROM sn_time_association WHERE id > 0;
	DELETE FROM sn_results WHERE id > 0;
	DELETE FROM sn_calc WHERE id > 0;
	DELETE FROM sn_curves WHERE id > 0;
	DELETE FROM sn_deltas WHERE id > 0;
	DELETE FROM sn_names WHERE id > 0;
    
    ALTER TABLE sn_band_association AUTO_INCREMENT = 1;
    ALTER TABLE sn_time_association AUTO_INCREMENT = 1;
    ALTER TABLE sn_results AUTO_INCREMENT = 1;
    ALTER TABLE sn_calc AUTO_INCREMENT = 1;
    ALTER TABLE sn_curves AUTO_INCREMENT = 1;
	ALTER TABLE sn_deltas AUTO_INCREMENT = 1;
    ALTER TABLE sn_names AUTO_INCREMENT = 1;

END $$
DELIMITER ;