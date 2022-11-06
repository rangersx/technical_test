package Controllers

import (
	"github.com/gofiber/fiber/v2"
	"github.com/rangersx/technical_test/backend/src/app/Common"
	"github.com/rangersx/technical_test/backend/src/app/Models"
	repositories "github.com/rangersx/technical_test/backend/src/app/Repositories"
	"strconv"
)

func GetAllTodoCategory(c *fiber.Ctx) error {
	id, errParseId := strconv.Atoi(c.Params("user_id"))
	if errParseId != nil {
		return errParseId
	}
	repository := repositories.TodoCategoryRepository{}
	todoCategory, err := repository.FindAllTodoCategory(uint(id))
	if err != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusNotFound,
				Message: err.Error(),
			})
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "get all successful",
		Data:    todoCategory,
	})
}

func GetAllByIdTodoCategory(c *fiber.Ctx) error {
	id, errParseId := strconv.Atoi(c.Params("id"))
	if errParseId != nil {
		return errParseId
	}
	repository := repositories.TodoCategoryRepository{}
	todoCategory, err := repository.FindByIdTodoCategory(id)
	if err != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusNotFound,
				Message: err.Error(),
			})
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "get by id successful",
		Data:    todoCategory,
	})
}

func CreateNewTodoCategory(c *fiber.Ctx) error {
	payload := new(Models.TodoCategory)
	errRequest := c.BodyParser(&payload)
	if errRequest != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusUnprocessableEntity,
				Message: errRequest.Error(),
			})
	}
	repository := repositories.TodoCategoryRepository{}
	todoCategory, errInsert := repository.CreateTodoCategory(*payload)
	if errInsert != nil {
		return errInsert
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "Create record successful",
		Data:    todoCategory,
	})
}

func UpdateTodoCategory(c *fiber.Ctx) error {
	id, errParseId := strconv.Atoi(c.Params("id"))
	if errParseId != nil {
		return errParseId
	}

	payload := new(Models.UpdatedTodoCategory)
	errRequest := c.BodyParser(&payload)
	if errRequest != nil {
		return c.JSON(
			Common.ErrorResponse{
				Status:  fiber.StatusUnprocessableEntity,
				Message: errRequest.Error(),
			})
	}

	repository := repositories.TodoCategoryRepository{}
	update, errUpdate := repository.UpdateTodoCategory(id, *payload)
	if errUpdate != nil {
		return errUpdate
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "Update record successful",
		Data:    update,
	})
}

func DeleteTodoCategory(c *fiber.Ctx) error {
	id, errParseId := strconv.Atoi(c.Params("id"))
	if errParseId != nil {
		return errParseId
	}
	repository := repositories.TodoCategoryRepository{}
	errDelete := repository.DeleteTodoCategory(id)
	if errDelete != nil {
		return errDelete
	}
	return c.JSON(Common.SuccessResponse{
		Status:  fiber.StatusOK,
		Message: "Delete record successful",
	})
}
