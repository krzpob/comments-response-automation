ALTER TABLE access_tokens
    ADD COLUMN id_bin BINARY(16) NULL AFTER id;

UPDATE access_tokens
    SET id_bin = UUID_TO_BIN(id);

ALTER TABLE access_tokens
    DROP PRIMARY KEY,
    DROP COLUMN id,
    CHANGE COLUMN id_bin id BINARY(16) NOT NULL,
    ADD PRIMARY KEY (id);