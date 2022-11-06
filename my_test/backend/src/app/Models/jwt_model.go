package Models

type UserInfo struct {
	FullName string `validate:"required"`
	Email    string `validate:"required,min=3,email"`
}

type JWTResult struct {
	Token string `json:"token"`
	Exp   int    `json:"exp"`
}

type JWTResponse struct {
	JWTResult
	User UserInfo `json:"user"`
}
