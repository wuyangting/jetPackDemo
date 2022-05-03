package com.example.myapplication.room

import androidx.room.*

@Dao
interface StudentDao {
    @Insert
    fun insertStudent(student: StudentBean)

    @Delete
    fun deleteStudent(student: StudentBean)

    @Update
    fun updateStudent(student: StudentBean)

    @Query("select * from student")
    fun getStudentList():List<StudentBean>

    @Query("select * from student where id=:id")
    fun getStudentById(id:Int):List<StudentBean>
}