package Repositories

import (
	"github.com/rangersx/technical_test/backend/src/app/Models"
	dbconn "github.com/rangersx/technical_test/backend/src/database"
)

type UserRepository struct {
}

func (u UserRepository) CreateUser(user *Models.User) error {
	db, err := dbconn.GetPostgresqlConnection()
	if err != nil {
		return nil
	}
	err = db.Create(&user).Error
	if err != nil {
		return nil
	}
	return nil
}

func (u UserRepository) FindByEmailUser(email string, user *Models.User) (err error) {
	db, _ := dbconn.GetPostgresqlConnection()
	err = db.Where("email = ?", email).First(&user).Error
	return
}

func (u UserRepository) FindByIdUser(id int) (*Models.User, error) {
	var user Models.User
	db, _ := dbconn.GetPostgresqlConnection()
	err := db.Where("id = ?", id).First(&user).Error
	if err != nil {
		return nil, err
	}
	return &user, nil
}
