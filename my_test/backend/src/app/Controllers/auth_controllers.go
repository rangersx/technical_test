package Controllers

import (
	"github.com/gofiber/fiber/v2"
	"github.com/rangersx/technical_test/backend/src/app/Common"
	"github.com/rangersx/technical_test/backend/src/app/Models"
	repositories "github.com/rangersx/technical_test/backend/src/app/Repositories"
	"github.com/rangersx/technical_test/backend/src/app/Services"
	"golang.org/x/crypto/bcrypt"
)

func Register(c *fiber.Ctx) error {
	payload := new(Models.User)
	errRequest := c.BodyParser(&payload)
	if errRequest != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusUnprocessableEntity,
				Message: errRequest.Error(),
			})
	}
	hashPassword, _ := bcrypt.GenerateFromPassword([]byte(payload.Password), bcrypt.DefaultCost)
	user := &Models.User{
		FullName: payload.FullName,
		Email:    payload.Email,
		Password: string(hashPassword),
	}
	repository := repositories.UserRepository{}
	errFindBy := repository.FindByEmailUser(payload.Email, user)
	if errFindBy == nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusBadRequest,
				Message: "email is already exists",
			})
	}
	repository.CreateUser(user)

	// generate token
	accessToken := Services.GenerateToken(user)

	data := Models.JWTResponse{
		JWTResult: accessToken,
		User: Models.UserInfo{
			FullName: payload.FullName,
			Email:    payload.Email,
		},
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "Register successful",
		Data:    data,
	})
}

func Login(c *fiber.Ctx) error {
	payload := new(Models.User)
	err1 := c.BodyParser(&payload)
	if err1 != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusUnprocessableEntity,
				Message: err1.Error(),
			})
	}
	user := &Models.User{}
	repository := repositories.UserRepository{}

	err2 := repository.FindByEmailUser(payload.Email, user)
	if err2 != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusInternalServerError,
				Message: err2.Error(),
			})
	}

	err3 := bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(payload.Password))
	if err3 != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusInternalServerError,
				Message: err3.Error(),
			})
	}

	// generate token
	accessToken := Services.GenerateToken(user)
	data := Models.JWTResponse{
		JWTResult: accessToken,
		User: Models.UserInfo{
			FullName: payload.FullName,
			Email:    payload.Email,
		},
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "Login successful",
		Data:    data,
	})
}
