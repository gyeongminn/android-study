package com.example.week27

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import retrofit2.http.GET
import retrofit2.http.Path


data class Owner(val login: String)
data class Repo(val name: String, val owner: Owner, val url: String)

interface RestApi {
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user: String): List<Repo>
}

@Entity
data class RepoD(
    @PrimaryKey val name: String,
    val owner: String,
)

@Dao
interface MyDao {
    @Query("SELECT * FROM RepoD")
    fun getAll(): LiveData<List<RepoD>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<RepoD>)
}

@Database(entities = [RepoD::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract val myDao: MyDao

    companion object {
        private var INSTANCE: MyDatabase? = null
        fun getDatabase(context: Context): MyDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context, MyDatabase::class.java, "repos_database"
                ).build()
            }
            return INSTANCE as MyDatabase
        }
    }
}
