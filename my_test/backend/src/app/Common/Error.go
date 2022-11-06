package Common

type ErrorResponse struct {
	Status  int     `json:"status"`
	Message string  `json:"message"`
	Errors  []Error `json:"errors"`
}

type Error struct {
	Field   string `json:"field"`
	Message string `json:"message"`
}
