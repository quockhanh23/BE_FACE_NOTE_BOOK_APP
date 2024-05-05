
// Xem index của bảng
SHOW INDEX FROM user_table;


// Xem engine của schema
SHOW TABLE STATUS FROM test_engine;

// Đổi engine từng bảng
ALTER TABLE image ENGINE = InnoDB;
                            MyISAM


// If trong Mysql

SELECT count(*)
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'test_engine'
  and table_name = 'test'
  and COLUMN_NAME = 'new_column1'
    INTO @columnCount;

set @alterStatement = IF(@columnCount = 1,
                         'ALTER TABLE test_engine.test ADD COLUMN new_column1 VARCHAR(50)', '');


PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
SET @columnCount = NULL;
SET @alterStatement = NULL;

select @columnCount;
select @alterStatement;
