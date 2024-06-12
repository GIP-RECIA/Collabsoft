-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema collabsoft
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema collabsoft
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `collabsoft` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci ;
USE `collabsoft` ;

-- -----------------------------------------------------
-- Table `collabsoft`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `collabsoft`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `cas_uid` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `collabsoft`.`associated_app`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `collabsoft`.`associated_app` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `enabled` TINYINT NOT NULL DEFAULT 0,
  `slug` VARCHAR(45) NOT NULL,
  `primary_color` VARCHAR(45) NULL,
  `icon_path` VARCHAR(45) NULL,
  `extension` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `collabsoft`.`file`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `collabsoft`.`file` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uuid` VARCHAR(255) NOT NULL DEFAULT 'uuid()',
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(255) NULL,
  `data` LONGTEXT NOT NULL,
  `creator_id` INT NOT NULL,
  `creation_date` DATETIME NOT NULL DEFAULT NOW(),
  `last_editor_id` INT NOT NULL,
  `edition_date` DATETIME NOT NULL DEFAULT NOW(),
  `associated_app_id` INT NOT NULL,
  `public` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_creator_idx` (`creator_id` ASC),
  INDEX `fk_associated_app_idx` (`associated_app_id` ASC),
  INDEX `fk_last_editor_idx` (`last_editor_id` ASC),
  CONSTRAINT `fk_creator`
    FOREIGN KEY (`creator_id`)
    REFERENCES `collabsoft`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_associated_app`
    FOREIGN KEY (`associated_app_id`)
    REFERENCES `collabsoft`.`associated_app` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_last_editor`
    FOREIGN KEY (`last_editor_id`)
    REFERENCES `collabsoft`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `collabsoft`.`file_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `collabsoft`.`file_history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `file_id` INT NOT NULL,
  `data` LONGTEXT NOT NULL,
  `creation_date` DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`id`, `file_id`),
  INDEX `fk_file_history_file_idx` (`file_id` ASC),
  CONSTRAINT `fk_file_history_file`
    FOREIGN KEY (`file_id`)
    REFERENCES `collabsoft`.`file` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `collabsoft`.`collaboration`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `collabsoft`.`collaboration` (
  `user_id` INT NOT NULL,
  `file_id` INT NOT NULL,
  `role` SMALLINT NULL,
  PRIMARY KEY (`user_id`, `file_id`),
  INDEX `fk_collaboration_file_idx` (`file_id` ASC),
  CONSTRAINT `fk_collaboration_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `collabsoft`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_collaboration_file`
    FOREIGN KEY (`file_id`)
    REFERENCES `collabsoft`.`file` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `collabsoft`.`metadata`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `collabsoft`.`metadata` (
  `user_id` INT NOT NULL,
  `file_id` INT NOT NULL,
  `starred` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`, `file_id`),
  INDEX `fk_metadata_user_idx` (`user_id` ASC),
  INDEX `fk_metadata_file_idx` (`file_id` ASC),
  CONSTRAINT `fk_metadata_file`
    FOREIGN KEY (`file_id`)
    REFERENCES `collabsoft`.`file` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_metadata_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `collabsoft`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
