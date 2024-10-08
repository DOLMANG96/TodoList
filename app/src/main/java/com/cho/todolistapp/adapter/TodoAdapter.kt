package com.cho.todolistapp.adapter

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cho.todolistapp.database.TodoDatabase
import com.cho.todolistapp.databinding.DialogEditBinding
import com.cho.todolistapp.databinding.ListItemTodoBinding
import com.cho.todolistapp.model.TodoInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){

    private var lstTodo : ArrayList<TodoInfo> = ArrayList()
    private lateinit var roomDatabase: TodoDatabase

    fun addListItem(todoItem: TodoInfo) {
        lstTodo.add(0,todoItem)
    }

//    inner class
    inner class TodoViewHolder(private val binding: ListItemTodoBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem : TodoInfo) {
            //리스트 뷰 데이터를 UI에 연동
            binding.tvContent.setText(todoItem.todoContent)
            binding.tvDate.setText(todoItem.todoDate)

            //리스트 삭제 버튼
            binding.btnRemove.setOnClickListener {
                // 삭제 이미지 클릭 내부 로직 수행

                AlertDialog.Builder(binding.root.context)
                    .setTitle("[주의]")
                    .setMessage("데이터는 복구 되지 않습니다.\n삭제하시겠습니까?")
                    .setPositiveButton("제거", DialogInterface.OnClickListener { dialogInterface, i ->
                        CoroutineScope(Dispatchers.IO).launch {
                        val innerLstTodo = roomDatabase.todoDao().getAllReadData()
                        for (item in innerLstTodo){
                            if(item.todoContent == todoItem.todoContent && item.todoDate == todoItem.todoDate){
                                roomDatabase.todoDao().deleteTododData(item)
                            }
                        }

                            roomDatabase.todoDao().deleteTododData(todoItem)
                            lstTodo.remove(todoItem)
                            (binding.root.context as Activity).runOnUiThread {
                                notifyDataSetChanged()
//                                토스트 메세지
                                Toast.makeText(binding.root.context,"제거되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    })

                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()

            }

//            수정 클릭
            binding.root.setOnClickListener {
                val bindingDialog = DialogEditBinding.inflate(LayoutInflater.from(binding.root.context), binding.root, false)
                bindingDialog.etMemo.setText(todoItem.todoContent)

                AlertDialog.Builder(binding.root.context)
                    .setTitle("To-do 남기기")
                    .setView(bindingDialog.root)
                    .setPositiveButton("수정완료",DialogInterface.OnClickListener { dialogInterface, i ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val innerLstTodo = roomDatabase.todoDao().getAllReadData()
                            for (item in innerLstTodo) {
                                if (item.todoContent == todoItem.todoContent && item.todoDate == todoItem.todoDate) {
                                    //data modify
                                    item.todoContent = bindingDialog.etMemo.text.toString()
                                    item.todoDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
                                    roomDatabase.todoDao().updateTodoData(item)
                                }
                            }

                            //ui modify
                            todoItem.todoContent = bindingDialog.etMemo.text.toString()
                            todoItem.todoDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
                            // arraylist 수정
                            lstTodo.set(adapterPosition, todoItem)

                            (binding.root.context as Activity).runOnUiThread {
                                notifyDataSetChanged()
                            }
                        }
                    })
                    .setNegativeButton("취소",DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show() //보여주기 기능
            }

        }
    }

    // 뷰폴더가 생성됨 (각 리스트 아이템 1개씩 구성될 때마다 이 오버라이드 메소드가 호출됨.)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoViewHolder {
        val binding = ListItemTodoBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        //        룸 데이터베이스 초기화
        roomDatabase = TodoDatabase.getInstance(binding.root.context)!!

        return TodoViewHolder(binding)
    }

    // 뷰홀더가 바인딩(결합이 이루어질 때 처리될 메소드)
    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
        holder.bind(lstTodo[position])
    }

    override fun getItemCount(): Int {
        return lstTodo.size
    }

}