package database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.kmp.kmpapp2.AppDatabase

actual class DriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return NativeSqliteDriver(schema = AppDatabase.Schema.synchronous(), name = "app.db")
    }
}