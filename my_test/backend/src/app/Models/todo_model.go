package Models

import (
	"gorm.io/gorm"
	"time"
)

type Todo struct {
	ID          uint   `json:"id" validate:"required,uuid"`
	Title       string `json:"title" validate:"required,lte=255"`
	Description string `json:"description" `
	UserID      uint   `json:"user_id" validate:"required"`
	Progress    int    `json:"progress"`
	Deadline    string `json:"deadline"`
	CreatedAt   time.Time
	UpdatedAt   time.Time
	DeletedAt   gorm.DeletedAt `gorm:"index"`
}

type UpdatedTodo struct {
	Title       string `json:"title" validate:"required,lte=255"`
	Description string `json:"description" `
	UserID      uint   `json:"user_id" validate:"required"`
	Progress    int    `json:"progress"`
	Deadline    string `json:"deadline"`
	UpdatedAt   time.Time
}
