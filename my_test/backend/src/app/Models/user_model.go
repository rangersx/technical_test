package Models

import (
	"gorm.io/gorm"
	"time"
)

type User struct {
	ID        uint   `json:"id" gorm:"primarykey;type:uuid"`
	FullName  string `json:"full_name" gorm:"type:varchar(128);not null" validate:"required"`
	Email     string `json:"email" gorm:"type:varchar(128);not null;unique" validate:"required,min=3,email"`
	Password  string `json:"password" validate:"required,min=3"`
	CreatedAt time.Time
	UpdatedAt time.Time
	DeletedAt gorm.DeletedAt `gorm:"index"`
}
