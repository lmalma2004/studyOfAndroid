package com.example.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()

        resultButton.setOnClickListener {
            saveData(heightEditText.text.toString().toInt(), weightEditText.text.toString().toInt())
            startActivity<ResultActivity>(
                "weight" to weightEditText.text.toString(),
                "height" to heightEditText.text.toString()
            )
        }
    }

    private fun saveData(height: Int, weight: Int){
        //프리퍼런스 매니저를 사용해 프리퍼런스 객체를 얻는다
        val pref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        //프리퍼런스 객체의 에디터 객체를 얻는다. 이 객체를 사용해 프리퍼런스에 데이터를 담을 수 있다.
        val editor = pref.edit()
        //에디터 객체에 put[데이터 타입] 형식의 메서드를 사용하여 키와 값 형태의 쌍으로 저장을 한다
        //put 메서드는 기본 타입은 모두 지원한다.
        editor.putInt("KEY_HEIGHT", height).putInt("KEY_WEIGHT", weight).apply()

    }

    private fun loadData(){
        val pref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        val height = pref.getInt("KEY_HEIGHT", 0)
        val weight = pref.getInt("KEY_WEIGHT", 0)

        if(height != 0 && weight != 0){
            heightEditText.setText(height.toString())
            weightEditText.setText(weight.toString())
        }
    }
}
