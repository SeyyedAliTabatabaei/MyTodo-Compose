package ir.mytodo

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import timber.log.Timber

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestRule {
    override fun apply(base: Statement?, description: Description?) = object : Statement() {

        override fun evaluate() {
            Dispatchers.setMain(StandardTestDispatcher())
            base?.evaluate()
            Dispatchers.resetMain()
        }
    }

}
