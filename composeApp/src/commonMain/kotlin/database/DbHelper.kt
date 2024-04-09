package database

import com.kmp.kmpapp2.AppDatabase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DbHelper(val driverFactory:DriverFactory) {

    private var db:AppDatabase? = null

    private val mutex = Mutex()

    suspend fun <Result:Any> withDatabase(block:suspend (AppDatabase)->Result) = mutex.withLock {
       if (db == null){
           db= createDb(driverFactory)
       }
        return@withLock block(db!!)
    }


  private  suspend fun createDb(driverFactory: DriverFactory):AppDatabase{
      return AppDatabase(driver = driverFactory.createDriver())
  }
}