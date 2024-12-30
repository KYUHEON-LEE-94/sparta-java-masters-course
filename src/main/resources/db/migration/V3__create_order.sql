CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL COMMENT '연관된 사용자 ID',
                        total_price DECIMAL(10, 2) NOT NULL,
                        status VARCHAR(20) DEFAULT 'pending' COMMENT 'pending, completed, canceled',
                        shipping_address TEXT NOT NULL,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);