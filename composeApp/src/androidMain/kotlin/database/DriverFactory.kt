package database

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.kmp.kmpapp2.AppDatabase
import io.ktor.util.reflect.*

actual class DriverFactory(val context:Context) {
    actual suspend fun createDriver(): SqlDriver{
        return AndroidSqliteDriver(schema = AppDatabase.Schema.synchronous(), context = context, name = "app.db")
    }
}