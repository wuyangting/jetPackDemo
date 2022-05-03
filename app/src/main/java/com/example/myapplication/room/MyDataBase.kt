package com.example.myapplication.room

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
//声明
@Database(entities = [StudentBean::class],version = 2)
abstract class MyDataBase : RoomDatabase() {

     companion object{
         private const val DATABASE_NAME="my_db"
         private var instance:MyDataBase?=null
       fun getInstance(context:Context):MyDataBase{
           instance?.let {
               return it
           }
           //创建Room数据库需要三个参数：1.上下文context，2.数据库Class  3.数据库名字db结尾
           //会通过反射创建数据库ClassImpl的实例  数据库Class名字和数据库名称
           //build方法会反射创建M有MyDataBaseImpl类并调用该类的init方法
           return Room.databaseBuilder(context.applicationContext,MyDataBase::class.java,
               DATABASE_NAME)
               .addCallback(object : Callback() {

                   override fun onCreate(db: SupportSQLiteDatabase) {
                       super.onCreate(db)
                       Log.i("wwwwwwwwwwwwwww", "RoomDataBaseonCreate: ")
                   }

                   override fun onOpen(db: SupportSQLiteDatabase) {
                       super.onOpen(db)
                       Log.i("wwwwwwwwwwwwwww", "RoomDataBaseoonOpen ")
                   }

                   override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                       super.onDestructiveMigration(db)
                       Log.i("wwwwwwwwwwwwwww", "数据库被破坏 ")
                   }
               }).addMigrations(object : Migration(1,2) {
                   override fun migrate(database: SupportSQLiteDatabase) {
                       database.execSQL("ALTER TABLE student "
                               + " ADD COLUMN sex TEXT NOT NULL DEFAULT '女'")
                   }
               })

               .build()
       }
    }

    abstract fun getStudentDao():StudentDao

}