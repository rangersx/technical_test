package Repositories

import (
	"errors"
	"github.com/rangersx/technical_test/backend/src/app/Models"
	dbconn "github.com/rangersx/technical_test/backend/src/database"
	"gorm.io/gorm"
)

type TodoRepository struct {
}

func (u TodoRepository) FindAllByUserIdTodo(userId int) ([]Models.Todo, error) {
	var todo []Models.Todo
	db, _ := dbconn.GetPostgresqlConnection()
	err := db.Where("user_id = ?", userId).Find(&todo).Error
	if errors.Is(err, gorm.ErrRecordNotFound) && err != nil {
		return todo, err
	}
	return todo, nil
}

func (u TodoRepository) FindByUserIdByProgressTodo(userId int, id int) ([]Models.Todo, error) {
	var todo []Models.Todo
	db, _ := dbconn.GetPostgresqlConnection()
	err := db.Where("user_id = ?", userId).Where("progress = ?", id).Find(&todo).Error
	if errors.Is(err, gorm.ErrRecordNotFound) && err != nil {
		return todo, err
	}
	return todo, nil
}

func (u TodoRepository) CreateTodo(todo Models.Todo) (*Models.Todo, error) {
	db, err := dbconn.GetPostgresqlConnection()
	if err != nil {
		return nil, err
	}
	err = db.Create(&todo).Error
	if errors.Is(err, gorm.ErrRecordNotFound) && err != nil {
		return nil, err
	}
	return &todo, nil
}

func (u TodoRepository) UpdateTodo(id int, updatedTodo Models.UpdatedTodo) (*Models.Todo, error) {
	todo := new(Models.Todo)
	db, _ := dbconn.GetPostgresqlConnection()
	err := db.Find(&todo, id).First(&todo).Error
	if errors.Is(err, gorm.ErrRecordNotFound) && err != nil {
		return nil, err
	}
	todo.Title = updatedTodo.Title
	todo.Description = updatedTodo.Description
	todo.Deadline = updatedTodo.Deadline
	todo.UserID = updatedTodo.UserID
	todo.Progress = updatedTodo.Progress
	db.Save(&todo)
	return todo, nil
}

func (u TodoRepository) DeleteTodo(id int) error {
	var todo Models.Todo
	db, _ := dbconn.GetPostgresqlConnection()
	err := db.First(&todo, id).Error
	if errors.Is(err, gorm.ErrRecordNotFound) && err != nil {
		return err
	}
	db.Delete(&todo)
	return nil
}
