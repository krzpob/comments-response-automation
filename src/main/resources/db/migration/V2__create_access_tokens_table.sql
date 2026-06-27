CREATE TABLE access_tokens (
    id                CHAR(36)       PRIMARY KEY,
    owner_ig_id       VARCHAR(255)   NOT NULL,
    owner_username    VARCHAR(255)   NOT NULL,
    token             TEXT           NOT NULL,
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at        TIMESTAMP      NOT NULL,
    refreshed_at      TIMESTAMP      NULL,
    token_type        VARCHAR(255)   NOT NULL,
    INDEX idx_access_tokens_owner_ig_id (owner_ig_id),
    INDEX idx_access_tokens_owner_username (owner_username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;