CREATE TABLE order_items (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT NOT NULL COMMENT '연관된 주문 ID',
                             product_id BIGINT NOT NULL COMMENT '연관된 상품 ID',
                             quantity INT NOT NULL,
                             price DECIMAL(10, 2) NOT NULL,
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);