package database

import (
	"fmt"
	"github.com/rangersx/technical_test/backend/src/app/Models"
	"github.com/rangersx/technical_test/backend/src/config"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"sync"
)

var lock = &sync.Mutex{}
var DBConn *gorm.DB

func GetPostgresqlConnection() (*gorm.DB, error) {
	var err error
	dsn := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=disable TimeZone=Asia/Jakarta",
		config.POSTGRES_HOST,
		config.POSTGRES_USERNAME,
		config.POSTGRES_PASSWORD,
		config.POSTGRES_DBNAME,
		config.POSTGRES_PORT,
	)
	lock.Lock()
	DBConn, err = gorm.Open(postgres.Open(dsn), &gorm.Config{})
	lock.Unlock()
	if err != nil {
		return nil, err
	}
	fmt.Println("Database connected!🔥")
	if config.POSTGRES_IS_MIGRATE {
		DBConn.AutoMigrate(&Models.User{})
		DBConn.AutoMigrate(&Models.Todo{})
		DBConn.AutoMigrate(&Models.TodoCategory{})
	}
	fmt.Println("Migrate DB!🔥")
	return DBConn, nil
}
