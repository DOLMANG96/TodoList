package com.cho.todolistapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cho.todolistapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

//    lateinit => 늦은 초기화
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvName.setText("안녕하세요 조민환입니다.")
        binding.tvTest.setText("나 화이팅!")

        println("cho onCreate!!")
    }

    override fun onStart() {
        super.onStart()

        //Activity 사용자에게 보여지기 직전에 호출됨.

        println("cho onStart!!")
    }

    override fun onResume() {
        super.onResume()
        //Activity가 사용자랑 상호작용 하기 직전에 호출됨. (시작 or 재개 상태)
        println("cho onResume!!!")
    }

    override fun onPause() {
        //다른 Activity가 보여지게 될 때 호출됨. (정지 상태)
        super.onPause()
        println("cho onPause!!!")
    }

    override fun onStop() {
        // Activity가 사용자에게 완전히 보여지지 않을 때 호출됨.
        super.onStop()
        println("cho onStop!!!")
    }

    override fun onDestroy() {
        // Activity가 소멸(제거)될 때 호출됨.
        super.onDestroy()
        println("cho onDestroy!!!")
    }

    override fun onRestart() {
        // Activity가 멈췄다가 다시 시작되기 전에 호출됨.
        super.onRestart()
        println("cho onRestart!!!")
    }
}