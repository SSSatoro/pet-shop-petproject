CREATE TABLE password_reset_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,


    user_id INT NOT NULL,

    expiry_date DATETIME NOT NULL,

    CONSTRAINT fk_user_password_token FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);