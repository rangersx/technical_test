package Controllers

import (
	"github.com/gofiber/fiber/v2"
	"github.com/rangersx/technical_test/backend/src/app/Common"
	"github.com/rangersx/technical_test/backend/src/app/Models"
	repositories "github.com/rangersx/technical_test/backend/src/app/Repositories"
	"strconv"
)

func GetAllTodo(c *fiber.Ctx) error {
	userId, errParseUserId := strconv.Atoi(c.Params("user_id"))
	if errParseUserId != nil {
		return errParseUserId
	}
	repository := repositories.TodoRepository{}
	todo, err := repository.FindAllByUserIdTodo(userId)
	if err != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusNotFound,
				Message: err.Error(),
			})
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "Get All Todo by UserID successful",
		Data:    todo,
	})
}

func GetAllByIdByUserIdTodo(c *fiber.Ctx) error {
	userId, errParseUserId := strconv.Atoi(c.Params("user_id"))
	if errParseUserId != nil {
		return errParseUserId
	}
	id, errParseId := strconv.Atoi(c.Params("id"))
	if errParseId != nil {
		return errParseId
	}
	repository := repositories.TodoRepository{}
	todo, err := repository.FindByUserIdByProgressTodo(userId, id)
	if err != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusNotFound,
				Message: err.Error(),
			})
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "Get All Todo By Id successful",
		Data:    todo,
	})
}

func CreateNewTodo(c *fiber.Ctx) error {
	payload := new(Models.Todo)
	errRequest := c.BodyParser(&payload)
	if errRequest != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusUnprocessableEntity,
				Message: errRequest.Error(),
			})
	}

	repository := repositories.TodoRepository{}
	user, errInsert := repository.CreateTodo(*payload)
	if errInsert != nil {
		return errInsert
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "Create record successful",
		Data:    user,
	})
}

func UpdateTodo(c *fiber.Ctx) error {
	id, errParseId := strconv.Atoi(c.Params("id"))
	if errParseId != nil {
		return errParseId
	}

	payload := new(Models.UpdatedTodo)
	errRequest := c.BodyParser(&payload)
	if errRequest != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusUnprocessableEntity,
				Message: errRequest.Error(),
			})
	}

	repository := repositories.TodoRepository{}
	update, errUpdate := repository.UpdateTodo(id, *payload)
	if errUpdate != nil {
		return errUpdate
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "Update record successful",
		Data:    update,
	})
}

func DeleteTodo(c *fiber.Ctx) error {
	id, errParseId := strconv.Atoi(c.Params("id"))
	if errParseId != nil {
		return errParseId
	}
	repository := repositories.TodoRepository{}
	errDelete := repository.DeleteTodo(id)
	if errDelete != nil {
		return errDelete
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "Delete record successful",
	})
}
