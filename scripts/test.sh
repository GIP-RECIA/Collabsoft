docker run -t -d --name mariadb --rm -v \
  --health-cmd="mysqladmin -uroot -proot ping" --health-interval=10s --health-timeout=10s --health-retries=10 \
  -p 3306:3306/tcp \
  -e "TZ=Europe/Paris" \
  -e "MYSQL_USER=root" \
  -e "MYSQL_ROOT_PASSWORD=root" \
  -e "MYSQL_DATABASE=collabsoft" \
  -e "MYSQL_DEFAULT_STORAGE_ENGINE=InnoDB" \
  -e "MYSQL_CHARACTER_SET_SERVER=utf8mb4" \
  -e "MYSQL_COLLATION_SERVER=utf8mb4_unicode_520_ci" \
  -e "MYSQL_INNODB_BUFFER_POOL_SIZE=2G" \
  -e "MYSQL_INNODB_DEFAULT_ROW_FORMAT=dynamic" \
  -e "MYSQL_INNODB_DATA_FILE_PATH=ibdata1:100M:autoextend:max:10G" \
  -e "MYSQL_INNODB_FLUSH_LOG_AT_TRX_COMMIT=1" \
  -e "MYSQL_INNODB_LOG_BUFFER_SIZE=64M" \
  -e "MYSQL_INNODB_LOG_FILE_SIZE=256M" \
  -e "MYSQL_INNODB_STRICT_MODE=ON" \
  -e "MYSQL_LOWER_CASE_TABLE_NAMES=1" \
  -e "MYSQL_MAX_CONNECT_ERRORS=100" \
  -e "MYSQL_MAX_CONNECTIONS=1000" \
  -e "MYSQL_QUERY_CACHE_LIMIT=10M" \
  -e "MYSQL_QUERY_CACHE_SIZE=0" \
  -e "MYSQL_QUERY_CACHE_TYPE=OFF" \
  wodby/mariadb:10.3 &
sleep 30
mysql -h127.0.0.1 -uroot -proot < ./src/test/resources/collabsoft.sql
./mvnw test -P test
docker stop mariadb