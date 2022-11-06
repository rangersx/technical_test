package Requests

type UserReq struct {
	Email    string `json:"email" validate:"required,min=3,email"`
	Password string `json:"password" validate:"required,min=3"`
}
