CREATE TABLE TargetDatabase (
    name            VARCHAR(64)     NOT NULL,
    jdbcUrl         VARCHAR(1024)   NOT NULL,
    username        VARCHAR(64)     NOT NULL,
    password        VARCHAR(64)     NOT NULL,
    healthCheckSQL  VARCHAR(256)    NOT NULL,
    usageStatus     VARCHAR(16)     NOT NULL,
    time            DATETIME(6)     NOT NULL,
    PRIMARY KEY (name)
);
