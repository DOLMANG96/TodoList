package com.cho.todolistapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TodoInfo {
    var todoContent: String = "" //메모내용
    var todoDate : String = "" // 메모 날짜

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

}