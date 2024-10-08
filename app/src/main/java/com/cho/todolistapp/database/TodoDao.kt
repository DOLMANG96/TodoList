package com.cho.todolistapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cho.todolistapp.model.TodoInfo

@Dao

interface TodoDao {

//    database table에 삽입
    @Insert
    fun insertTodoData(todoInfo: TodoInfo)
//    database table에 기존에 존재하는 데이터 수정
    @Update
    fun updateTodoData(todoInfo: TodoInfo)
//    database table에 기존에 존재하는 데이터 삭제
    @Delete
    fun deleteTododData(todoInfo: TodoInfo)
//    database table에 전체 데이터를 가지고 옴
    @Query("SELECT * FROM TodoInfo ORDER BY todoDate")
    fun getAllReadData() : List<TodoInfo>

}