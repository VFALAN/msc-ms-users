DELIMITER $$

CREATE PROCEDURE search_username(
    IN username VARCHAR(100),
    OUT count_of_users INT
)
BEGIN
    SELECT
        COUNT(user_name)
    INTO
        count_of_users
    FROM
        tuser
    WHERE
        user_name = username;
END$$

DELIMITER ;