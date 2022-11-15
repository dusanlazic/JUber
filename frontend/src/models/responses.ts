import { HttpStatusCode } from "@angular/common/http"

export interface ApiResponse {
    status: HttpStatusCode,
    message: string,
    errors?: any
}