package justbucket.ruherobase.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class UseCase<out Type, in Params>(private val context: CoroutineContext) where Type : Any {

    val ILLEGAL_EXCEPTION_MESSAGE = "Params can't be null"
    /*companion object {
        protected const val ILLEGAL_EXCEPTION_MESSAGE = "Params can't be null"
    }*/

    protected abstract suspend fun run(params: Params? = null): Type

    fun execute(onResult: ((Type) -> Unit)? = null, params: Params? = null) {
        val job = GlobalScope.async(context = Dispatchers.IO) { run(params) }
        GlobalScope.launch(context = context) { onResult?.invoke(job.await()) }
    }
}