package functional

sealed class Result<TSuccess, TError> {
    class Success<TSuccess, TError>(val value: TSuccess) : Result<TSuccess, TError>()
    class Failure<TSuccess, TError>(val error: TError) : Result<TSuccess, TError>()
}