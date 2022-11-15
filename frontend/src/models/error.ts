import { HttpStatusCode } from "@angular/common/http"

export type ResponseError = {
    status: HttpStatusCode,
    message: string,
    errors: any
}