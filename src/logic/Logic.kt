package logic

import functional.*

data class Data(val value: String?)

data class Request(val name : String, val data : Data)

sealed class Error {
    class ValidationError(val text : String) : Error()
    class DbError(val text : String) : Error()
}

interface Response
class OkResponse(val data : Data) : Response
class InvalidInputResponse(val message: String) : Response
class InternalServerErrorResponse() : Response

fun validate(request: Request): Result<Request, Error> =
        if (request.name.isNullOrEmpty()) Result.Failure(Error.ValidationError("Name can't be empty"))
        else Result.Success(request)

fun prepareData(request: Request) : Request =
        Request(request.name, Data(request.data.value ?: "---"))

fun updateDb(request: Request): Result<Request, Error> {
    try {
        Db().storeData(request.data)
        return Result.Success(request)
    } catch (ex: Exception) {
        return Result.Failure(Error.DbError(ex.message ?: "Unknown DB error"))
    }
}

fun sendNotification(request: Request) {
    println("NOTIFICATION FOR ${request.name}")
}

fun createResponse(result: Result<Request, Error>) : Response =
        when (result) {
            is Result.Success -> OkResponse(result.value.data)
            is Result.Failure -> when (result.error) {
                // If we remove one of the following lines, the compiler will generate an error including
                // the name of the type that is not covered by the "when" branches
                is Error.ValidationError -> InvalidInputResponse(result.error.text)
                is Error.DbError -> InternalServerErrorResponse()
            }
        }

val processRequest = ::validate
        .map(::prepareData)
        .bind(::updateDb)
        .pass(::sendNotification)
        .compose(::createResponse)

val processRequestAlternate =
        ::validate map ::prepareData bind ::updateDb pass ::sendNotification compose ::createResponse
