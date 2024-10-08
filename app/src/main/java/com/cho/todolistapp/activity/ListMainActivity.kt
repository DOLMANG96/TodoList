package com.cho.todolistapp.activity

import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.room.RoomDatabase
import com.cho.todolistapp.adapter.TodoAdapter
import com.cho.todolistapp.database.TodoDatabase
import com.cho.todolistapp.databinding.ActivityListMainBinding
import com.cho.todolistapp.databinding.DialogEditBinding
import com.cho.todolistapp.model.TodoInfo
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class ListMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListMainBinding
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var roomDatabase: TodoDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 어댑터 인스턴스 생성
        todoAdapter = TodoAdapter()

//        리사이클뷰에 어댑터 세팅
        binding.rvTodo.adapter = todoAdapter

//        룸 데이터베이스 초기화
        roomDatabase = TodoDatabase.getInstance(applicationContext)!!

//        전체 데이터 Load(비동기)
        CoroutineScope(Dispatchers.IO).launch {
            val lstTodo = roomDatabase.todoDao().getAllReadData() as ArrayList<TodoInfo>
            for (todoItem in lstTodo) {
                todoAdapter.addListItem(todoItem)
            }
//            UI Thread 에서 처리해야함.
            runOnUiThread {
                todoAdapter.notifyDataSetChanged()
            }

        }


//        버튼 하기 작성 클릭
        binding.btnWrite.setOnClickListener {
            val bindingDialog = DialogEditBinding.inflate(LayoutInflater.from(binding.root.context), binding.root, false)

            AlertDialog.Builder(this)
                .setTitle("To-do 남기기")
                .setView(bindingDialog.root)
                .setPositiveButton("작성완료",DialogInterface.OnClickListener { dialogInterface, i ->
                    val todoItem = TodoInfo()
                    todoItem.todoContent = bindingDialog.etMemo.text.toString()
                    todoItem.todoDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
                    todoAdapter.addListItem(todoItem) //어댑터의 전역변수의 arraylist 쪽에 아이템 추가하기 위한 메소드 호출

                    CoroutineScope(Dispatchers.IO).launch {
                        roomDatabase.todoDao().insertTodoData(todoItem) //데이터베이스 또한 클래스 삽입
                        runOnUiThread{
                            todoAdapter.notifyDataSetChanged() // 리스트 새로고침
                        }
                    }

                })
                .setNegativeButton("취소",DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show() //보여주기 기능
        }
    }

}
