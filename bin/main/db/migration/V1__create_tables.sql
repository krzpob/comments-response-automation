CREATE TABLE keyword_rules (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    keyword               VARCHAR(255)  NOT NULL,
    dm_template           TEXT          NOT NULL,
    comment_reply_template TEXT         NOT NULL,
    page_id               VARCHAR(255)  NULL,
    enabled               TINYINT(1)   NOT NULL DEFAULT 1,
    created_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_keyword_rules_page_id (page_id),
    INDEX idx_keyword_rules_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE comment_events (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id       VARCHAR(255)  NOT NULL,
    sender_id        VARCHAR(255)  NOT NULL,
    page_id          VARCHAR(255)  NOT NULL,
    media_id         VARCHAR(255)  NOT NULL,
    comment_text     TEXT          NOT NULL,
    matched_rule_id  BIGINT        NULL,
    dm_sent          TINYINT(1)   NOT NULL DEFAULT 0,
    comment_replied  TINYINT(1)   NOT NULL DEFAULT 0,
    processed_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_comment_events_comment_id (comment_id),
    INDEX idx_comment_events_sender_id (sender_id),
    INDEX idx_comment_events_page_id (page_id),
    INDEX idx_comment_events_processed_at (processed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
