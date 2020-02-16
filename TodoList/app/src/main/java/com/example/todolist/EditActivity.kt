package com.example.todolist

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*

class EditActivity : AppCompatActivity() {

    //MyApplication클래스에서 Realm을 초기화했다면 액티비티에서는
    //Realm.getDefaultInstance()메서드를 이용해 Realm객체의 인스턴스를 얻을 수 있다.
    val realm = Realm.getDefaultInstance() // realm 인스턴스 얻기
    val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //defaultValue : 반환값이 없을 때의 값
        val id = intent.getLongExtra("id", -1L)
        if(id == -1L)
            insertMode()
        else
            updateMode(id)

        calendarView.setOnDateChangeListener{view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun insertMode(){
        deleteFab.visibility = View.GONE
        doneFab.setOnClickListener{
            insertTodo()
        }
    }
    private fun updateMode(id: Long){
        val todo = realm.where<Todo>().equalTo("id", id).findFirst()!!
        todoEditText.setText(todo.title)
        calendarView.date = todo.date

        doneFab.setOnClickListener{
            updateTodo(id);
        }
        deleteFab.setOnClickListener {
            deleteTodo(id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close() // 인스턴스 해제
    }

    private fun insertTodo(){
        realm.beginTransaction() // 트랜잭션 시작

        val newItem = realm.createObject<Todo>(nextId())
        newItem.title = todoEditText.text.toString()
        newItem.date = calendar.timeInMillis

        realm.commitTransaction() //트랜잭션 종료 반영

        alert("내용이 추가되었습니다."){
            yesButton { finish() }
        }.show()
    }

    private fun updateTodo(id: Long){
        realm.beginTransaction()

        val updateItem = realm.where<Todo>().equalTo("id", id).findFirst()!!
        updateItem.title = todoEditText.text.toString()
        updateItem.date = calendar.timeInMillis

        realm.commitTransaction()

        alert("내용이 변경되었습니다."){
            yesButton { finish() }
        }.show()
    }

    private fun deleteTodo(id: Long){
        realm.beginTransaction()

        val deleteItem = realm.where<Todo>().equalTo("id", id).findFirst()!!
        deleteItem.deleteFromRealm()

        realm.commitTransaction()

        alert("내용이 삭제되었습니다"){
            yesButton { finish() }
        }.show()
    }

    private fun nextId(): Int{
        val maxId = realm.where<Todo>().max("id")
        if(maxId != null)
            return maxId.toInt() + 1
        return 0
    }

}
