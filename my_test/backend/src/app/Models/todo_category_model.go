package Models

import (
	"gorm.io/gorm"
	"time"
)

type TodoCategory struct {
	ID        uint   `json:"id" validate:"required,uuid"`
	Name      string `json:"name" validate:"required"`
	UserID    uint   `json:"user_id" validate:"required"`
	CreatedAt time.Time
	UpdatedAt time.Time
	DeletedAt gorm.DeletedAt `gorm:"index"`
}

type UpdatedTodoCategory struct {
	Name   string `json:"name" validate:"required"`
	UserID uint   `json:"user_id" validate:"required"`
}
