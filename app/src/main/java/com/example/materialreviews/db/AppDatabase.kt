package com.example.materialreviews.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.materialreviews.getInitialRestaurantsData

@Database(entities = [UserEntity::class, ImageEntity::class, RestaurantEntity::class, ReviewEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun imageDao(): ImageDao
    abstract fun reviewDao(): ReviewDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase {


            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().addCallback(object:Callback(){
                    override fun onCreate ( db: SupportSQLiteDatabase){
                        super.onCreate(db)
                        //val restaurants= getInitialrestaurantsData()
                        //for(restaurant in restaurants){
                           // db.execSQL("INSERT INTO restaurants VALUES ($restaurant.rid , $restaurant.name , $restaurant.sito , $restaurant.orario , $restaurant.address)")

                        //}
                    }

                }).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

    }
}

