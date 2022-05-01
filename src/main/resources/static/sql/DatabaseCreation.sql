DROP DATABASE IF EXISTS SUPERNOVAE;
CREATE DATABASE IF NOT EXISTS SUPERNOVAE;
USE SUPERNOVAE;

DROP TABLE IF EXISTS sn_names;
CREATE TABLE IF NOT EXISTS sn_names(
	`id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS sn_deltas;
CREATE TABLE IF NOT EXISTS sn_deltas(
	`id` INT NOT NULL AUTO_INCREMENT,
    `sn_id` INT NOT NULL,
    `delta_value` DOUBLE NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`sn_id`) REFERENCES sn_names (`id`)
);

DROP TABLE IF EXISTS sn_curves;
CREATE TABLE IF NOT EXISTS sn_curves (
	`id` INT NOT NULL AUTO_INCREMENT,
    `sn_id` INT NOT NULL,
    `mag_curve` DOUBLE NOT NULL,
    `lum_curve` DOUBLE NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`sn_id`) REFERENCES sn_names (`id`)
);

DROP TABLE IF EXISTS sn_calc;
CREATE TABLE IF NOT EXISTS sn_calc (
	`id` INT NOT NULL AUTO_INCREMENT,
    `sn_id` INT NOT NULL,
    `calc_res_1` DOUBLE NOT NULL,
    `calc_res_2` DOUBLE NOT NULL,
    PRIMARY KEY (`id`), 
    FOREIGN KEY (`sn_id`) REFERENCES sn_names (`id`)
);

DROP TABLE IF EXISTS sn_results;
CREATE TABLE IF NOT EXISTS sn_results(
	`id` INT NOT NULL AUTO_INCREMENT,
    `sn_id` INT NOT NULL,
    `band_max` DOUBLE NOT NULL,
    `band_min` DOUBLE NOT NULL,
    `band_delta_id` INT NOT NULL,
    `curve_id` INT NOT NULL,
    `calc_res_id` INT NOT NULL,
    PRIMARY KEY(`id`),
    FOREIGN KEY (`sn_id`) REFERENCES sn_names(`id`),
    FOREIGN KEY (`band_delta_id`) REFERENCES sn_deltas(`id`),
	FOREIGN KEY (`curve_id`) REFERENCES sn_curves(`id`),
    FOREIGN KEY (`calc_res_id`) REFERENCES sn_calc(`id`)
);

DROP TABLE IF EXISTS sn_band_association;
CREATE TABLE IF NOT EXISTS sn_band_association(
	`id` INT NOT NULL AUTO_INCREMENT, 
    `magnitude` DOUBLE NOT NULL,
    `luminosity` DOUBLE NOT NULL,
    `sn_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`sn_id`) REFERENCES sn_names(`id`)
);

DROP TABLE IF EXISTS sn_time_association;
CREATE TABLE IF NOT EXISTS sn_time_association(
	`id` INT NOT NULL AUTO_INCREMENT,
    `time_value` DOUBLE NOT NULL,
    `sn_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`sn_id`) REFERENCES sn_names(`id`)
);






