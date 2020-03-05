package com.example.tiltsensor

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager

class MainActivity : AppCompatActivity(), SensorEventListener{

    private lateinit var tiltView: TiltView

    //장치에 있는 센서를 사용하려면 먼저 센서 매니저에 대한 참조를 얻어야 한다.
    //이렇게 하려면 getSystemService() 메서드에 SENSOR_SERVICE상수를 전달하여 SensorManager클래스의 인스턴스를 만든다.
    //지연된 초기화를 사용하여 sensorManager 변수를 처음 사용할 때 getSystemService() 메서드로 SensorManager객체를 얻는다.
    private val sensorManager by lazy{
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onCreate(savedInstanceState)

        tiltView = TiltView(this)
        setContentView(tiltView)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    //센서값이 변경되면 호출된다
    override fun onSensorChanged(event: SensorEvent?) {
        //values[0] : x축 값 : 위로 기울이면 -10 ~ 0 , 아래로 기울이면 0 ~ 10
        //values[1] : y축 값 : 왼쪽으로 기울이면 -10 ~ 0 , 오른쪽 기울이면 0 ~ 10
        //values[2] : z축 값 : 미사용
        event?.let {
            Log.d("MainActivity", "onSensorChanged: x : " + "${event.values[0]}, y: ${event.values[1]}, z: ${event.values[2]}")
        }
        tiltView.onSensorEvent(event!!)
    }
}
