
CREATE TABLE category (
    cname varchar(255),
    prioritynum int CONSTRAINT priority CHECK (prioritynum >=1 AND prioritynum <= 4),
    PRIMARY KEY(cname)
);
CREATE TABLE person (
    hin int NOT NULL,
    pname varchar(255) NOT NULL,
    gender varchar(255) NOT NULL,
    dob date NOT NULL,
    phone varchar(255) NOT NULL,
    city varchar(255) NOT NULL,
    postalcd varchar(255) NOT NULL,
    streetad varchar(255) NOT NULL,
    regdate date NOT NULL,
    cname varchar(255) NOT NULL,
    PRIMARY KEY(hin),
    FOREIGN KEY (cname) REFERENCES category(cname)
);
CREATE TABLE vaccinfo (
    waitperiod int NOT NULL,
    doses int NOT NULL,
    vname varchar(255) NOT NULL,
    PRIMARY KEY (vname)
);
CREATE TABLE batch (
    batchnum int NOT NULL,
    expdate date NOT NULL,
    manufactdate date NOT NULL,
    numvials int NOT NULL,
    lname varchar(255) NOT NULL,
    vname varchar(255) NOT NULL,
    PRIMARY KEY(batchnum, vname),
    FOREIGN KEY(vname) REFERENCES vaccinfo(vname)

);
CREATE TABLE vacclocation(
    lname varchar(255) NOT NULL,
    city varchar(255) NOT NULL,
    postalcd varchar(255) NOT NULL,
    streetad varchar(255) NOT NULL,
    PRIMARY KEY (lname)
);
CREATE TABLE vaccdate(
    vdate date NOT NULL,
    lname varchar(255),
    PRIMARY KEY(vdate),
    FOREIGN KEY (lname) REFERENCES vacclocation(lname)
    
);
CREATE TABLE vial (
    vialnum int NOT NULL,
    vname varchar(255) NOT NULL,
    batchnum int NOT NULL,
    PRIMARY KEY(vialnum, vname, batchnum),
    FOREIGN KEY(batchnum, vname) REFERENCES batch(batchnum, vname),
  
);
CREATE TABLE slot(
    vtime time NOT NULL,
    vdate date NOT NULL,
    slotID int NOT NULL,
    lname varchar(255) NOT NULL,
    hin int NOT NULL,
    cnln int NOT NULL, 
    vialnum int NOT NULL,
    vname varchar(255) NOT NULL,
    batchnum int NOT NULL,
    asgndate date NOT NULL,
    PRIMARY KEY (slotID, lname, vdate, vtime),
    FOREIGN KEY (lname) REFERENCES vacclocation(lname),
    FOREIGN KEY (vdate) REFERENCES vaccdate(vdate),
    FOREIGN KEY (hin) REFERENCES person(hin),
    FOREIGN KEY (vialnum, batchnum, vname) REFERENCES vial(vialnum, batchnum, vname)
);



CREATE TABLE hospital(
    lname varchar(255) NOT NULL REFERENCES vacclocation(lname),
    PRIMARY KEY(lname)

);



CREATE TABLE nurse(
    cnln int NOT NULL,
    name varchar(255) NOT NULL,
    lname varchar(255) NOT NULL,
    PRIMARY KEY (cnln),
    FOREIGN KEY (lname) REFERENCES hospital(lname)
);
CREATE TABLE nursedeputed(
    cnln int NOT NULL,
    vdate date NOT NULL,
    lname varchar(255) NOT NULL,
    PRIMARY KEY(cnln,vdate,lname),
    FOREIGN KEY (vdate) REFERENCES vaccdate(vdate),
    FOREIGN KEY (lname) REFERENCES vacclocation(lname)
);
-- CREATE TABLE worksat (
--     cnln int NOT NULL,
--     lname varchar(255) NOT NULL,
--     PRIMARY KEY (cnln, lname),
--     FOREIGN KEY (cnln) REFERENCES nurse(cnln),
--     FOREIGN KEY (lname) REFERENCES hospital(lname)
-- );
