package functional

fun <T, U, Err> ((T) -> Result<U, Err>).bind() : (Result<T, Err>) -> Result<U, Err> =
        { input ->
            when (input) {
                is Result.Success -> this(input.value)
                is Result.Failure -> Result.Failure(input.error)
            }
        }

fun <T, U, Err> ((T) -> U).map() : (Result<T, Err>) -> Result<U, Err> =
        { input ->
            when (input) {
                is Result.Success -> Result.Success(this(input.value))
                is Result.Failure -> Result.Failure(input.error)
            }
        }

infix fun <T, U, V, Err> ((T) -> Result<U, Err>).bind(f2: (U) -> Result<V, Err>) : (T) -> Result<V, Err> =
        { input ->
            val result = this(input)
            when (result) {
                is Result.Success -> f2(result.value)
                is Result.Failure -> Result.Failure(result.error)
            }
        }

infix fun <T, U, V, Err> ((T) -> Result<U, Err>).map(f2: (U) -> V) : (T) -> Result<V, Err> =
        { input ->
            val result = this(input)
            when (result) {
                is Result.Success -> Result.Success(f2(result.value))
                is Result.Failure -> Result.Failure(result.error)
            }
        }

infix fun <T, U, Err> ((T) -> Result<U, Err>).pass(f2: (U) -> Unit) : (T) -> Result<U, Err> =
        { input ->
            val result = this(input)
            when (result) {
                is Result.Success -> {
                    f2(result.value); result
                }
                is Result.Failure -> Result.Failure(result.error)
            }
        }

infix fun <T, U, V> ((T) -> U).compose(f2: (U) -> V) : (T) -> V =
        { input -> f2(this(input)) }