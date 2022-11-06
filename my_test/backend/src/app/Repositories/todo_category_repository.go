package Repositories

import (
	"errors"
	"github.com/rangersx/technical_test/backend/src/app/Models"
	dbconn "github.com/rangersx/technical_test/backend/src/database"
	"gorm.io/gorm"
)

type TodoCategoryRepository struct {
}

func (u TodoCategoryRepository) FindAllTodoCategory(userId uint) ([]Models.TodoCategory, error) {
	var todoCategory []Models.TodoCategory
	db, _ := dbconn.GetPostgresqlConnection()
	err := db.Where("user_id = ?", userId).Find(&todoCategory).Error
	if errors.Is(err, gorm.ErrRecordNotFound) && err != nil {
		return todoCategory, err
	}
	return todoCategory, nil
}

func (u TodoCategoryRepository) FindByIdTodoCategory(id int) (Models.TodoCategory, error) {
	var todoCategory Models.TodoCategory
	db, _ := dbconn.GetPostgresqlConnection()
	err := db.First(&todoCategory, id).Error
	if errors.Is(err, gorm.ErrRecordNotFound) && err != nil {
		return todoCategory, err
	}
	return todoCategory, nil
}

func (u TodoCategoryRepository) CreateTodoCategory(todoCategory Models.TodoCategory) (*Models.TodoCategory, error) {
	db, err := dbconn.GetPostgresqlConnection()
	if err != nil {
		return nil, err
	}
	err = db.Create(&todoCategory).Error
	if errors.Is(err, gorm.ErrRecordNotFound) && err != nil {
		return nil, err
	}
	return &todoCategory, nil
}

func (u TodoCategoryRepository) UpdateTodoCategory(id int, updatedTodoCategory Models.UpdatedTodoCategory) (*Models.TodoCategory, error) {
	todoCategory := new(Models.TodoCategory)
	db, _ := dbconn.GetPostgresqlConnection()
	err := db.Find(&todoCategory, id).First(&todoCategory).Error
	if errors.Is(err, gorm.ErrRecordNotFound) && err != nil {
		return nil, err
	}
	todoCategory.Name = updatedTodoCategory.Name
	todoCategory.UserID = updatedTodoCategory.UserID
	db.Save(&todoCategory)
	return todoCategory, nil
}

func (u TodoCategoryRepository) DeleteTodoCategory(id int) error {
	var todoCategory Models.TodoCategory
	db, _ := dbconn.GetPostgresqlConnection()
	err := db.First(&todoCategory, id).Error
	if errors.Is(err, gorm.ErrRecordNotFound) && err != nil {
		return err
	}
	db.Delete(&todoCategory)
	return nil
}
