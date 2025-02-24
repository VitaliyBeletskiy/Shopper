package vibe.shopper.data.model

sealed class Result<out T, out E>

data class Success<out T>(val value: T) : Result<T, Nothing>() {
    override fun toString(): String = "Success($value)"
}

data class Failure<out E>(val error: E) : Result<Nothing, E>() {
    override fun toString(): String = "Failure($error)"
}

inline fun <T, E, R> Result<T, E>.fold(
    ifSuccess: (value: T) -> R,
    ifFailure: (error: E) -> R,
): R = when (this) {
    is Success -> ifSuccess(value)
    is Failure -> ifFailure(error)
}
