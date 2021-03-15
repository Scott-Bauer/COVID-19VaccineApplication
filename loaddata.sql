

LOAD DATA INFILE "C:\Users\scott\Desktop\p2data\category.csv" INTO TABLE category
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(cname, prioritynum)