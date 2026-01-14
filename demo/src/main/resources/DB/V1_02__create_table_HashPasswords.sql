CREATE TABLE HashPasswords (
    id              INT             NOT NULL,
    username        VARCHAR(64)     NOT NULL,
    password        VARCHAR(64)     NOT NULL,
    healthCheckSQL  VARCHAR(256)    NOT NULL,
    usageStatus     VARCHAR(16)     NOT NULL,
    time            DATETIME(6)     NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);