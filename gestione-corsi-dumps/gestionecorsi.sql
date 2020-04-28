-- MySQL Script generated by MySQL Workbench
-- Fri Apr 24 21:42:03 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema gestionecorsi
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gestionecorsi
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gestionecorsi` DEFAULT CHARACTER SET utf8 ;
USE `gestionecorsi` ;

-- -----------------------------------------------------
-- Table `gestionecorsi`.`dipartimento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestionecorsi`.`dipartimento` (
  `iddipartimento` INT NOT NULL AUTO_INCREMENT,
  `denominazione` VARCHAR(45) NOT NULL,
  `descrizione` VARCHAR(255) NULL,
  `paese` VARCHAR(255) NULL,
  `indirizzo` VARCHAR(255) NULL,
  `telefono` VARCHAR(45) NULL,
  PRIMARY KEY (`iddipartimento`),
  UNIQUE INDEX `denominazione_UNIQUE` (`denominazione` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestionecorsi`.`docente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestionecorsi`.`docente` (
  `iddocente` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `nome` VARCHAR(45) NULL,
  `cognome` VARCHAR(45) NULL,
  `telefono` VARCHAR(45) NULL,
  `dipartimento_iddipartimento` INT NOT NULL,
  PRIMARY KEY (`iddocente`, `dipartimento_iddipartimento`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  INDEX `fk_docente_dipartimento_idx` (`dipartimento_iddipartimento` ASC),
  CONSTRAINT `fk_docente_dipartimento`
    FOREIGN KEY (`dipartimento_iddipartimento`)
    REFERENCES `gestionecorsi`.`dipartimento` (`iddipartimento`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestionecorsi`.`corso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestionecorsi`.`corso` (
  `idcorso` INT NOT NULL AUTO_INCREMENT,
  `codice` VARCHAR(45) NOT NULL,
  `ssd` VARCHAR(45) NOT NULL,
  `denominazione` VARCHAR(45) NULL,
  `codicemutuazione` VARCHAR(45) NULL,
  `cfu` INT NULL,
  `numstudenti` INT NULL,
  `oretotali` INT NOT NULL,
  `parametro` VARCHAR(45) NULL,
  `anno` VARCHAR(25) NOT NULL,
  `periodo` INT NOT NULL,
  `dipartimento_iddipartimento` INT NOT NULL,
  PRIMARY KEY (`idcorso`, `dipartimento_iddipartimento`),
  UNIQUE INDEX `corso_UNIQUE` (`codice` ASC, `ssd` ASC, `anno` ASC, `periodo` ASC),
  INDEX `fk_corso_dipartimento1_idx` (`dipartimento_iddipartimento` ASC),
  CONSTRAINT `fk_corso_dipartimento1`
    FOREIGN KEY (`dipartimento_iddipartimento`)
    REFERENCES `gestionecorsi`.`dipartimento` (`iddipartimento`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestionecorsi`.`assegnazione`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestionecorsi`.`assegnazione` (
  `docente_iddocente` INT NOT NULL,
  `corso_idcorso` INT NOT NULL,
  `contratto` ENUM("ISTITU", "AFF_RET", "EST") NOT NULL,
  `tipologia` ENUM("LEZIONE", "ESERCITAZIONE", "TUTORATO") NOT NULL,
  `ore` INT NOT NULL,
  PRIMARY KEY (`docente_iddocente`, `corso_idcorso`, `tipologia`, `contratto`),
  INDEX `fk_docente_has_corso_corso1_idx` (`corso_idcorso` ASC),
  INDEX `fk_docente_has_corso_docente1_idx` (`docente_iddocente` ASC),
  INDEX `assegnazione_UNIQUE` (`docente_iddocente` ASC, `corso_idcorso` ASC, `tipologia` ASC),
  CONSTRAINT `fk_docente_has_corso_docente1`
    FOREIGN KEY (`docente_iddocente`)
    REFERENCES `gestionecorsi`.`docente` (`iddocente`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_docente_has_corso_corso1`
    FOREIGN KEY (`corso_idcorso`)
    REFERENCES `gestionecorsi`.`corso` (`idcorso`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestionecorsi`.`corsodilaurea`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestionecorsi`.`corsodilaurea` (
  `idcorsodilaurea` INT NOT NULL AUTO_INCREMENT,
  `denominazione` VARCHAR(255) NOT NULL,
  `codice` VARCHAR(45) NOT NULL DEFAULT 'Non Assegnato',
  `anno` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`idcorsodilaurea`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestionecorsi`.`cdlassegnazione`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestionecorsi`.`cdlassegnazione` (
  `corsodilaurea_idcorsodilaurea` INT NOT NULL,
  `corso_idcorso` INT NOT NULL,
  `annorif` INT NULL,
  PRIMARY KEY (`corsodilaurea_idcorsodilaurea`, `corso_idcorso`),
  INDEX `fk_corsodilaurea_has_corso_corso1_idx` (`corso_idcorso` ASC),
  INDEX `fk_corsodilaurea_has_corso_corsodilaurea1_idx` (`corsodilaurea_idcorsodilaurea` ASC),
  UNIQUE INDEX `fk_cdlas_un` (`corsodilaurea_idcorsodilaurea` ASC, `corso_idcorso` ASC),
  CONSTRAINT `fk_corsodilaurea_has_corso_corsodilaurea1`
    FOREIGN KEY (`corsodilaurea_idcorsodilaurea`)
    REFERENCES `gestionecorsi`.`corsodilaurea` (`idcorsodilaurea`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_corsodilaurea_has_corso_corso1`
    FOREIGN KEY (`corso_idcorso`)
    REFERENCES `gestionecorsi`.`corso` (`idcorso`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
