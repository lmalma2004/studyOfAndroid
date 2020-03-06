package com.example.flashlight

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TorchService : Service() {

    private var isRunning = false
    private val torch: Torch by lazy{
        Torch(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            //앱에서 실행할 경우
            "on" -> {
                torch.flashOn()
                isRunning = true
            }
            "off" -> {
                torch.flashOff()
                isRunning = false
            }
            //서비스에서 실행할 경우
            else -> {
                isRunning = !isRunning
                if(isRunning){
                    torch.flashOn()
                }
                else{
                    torch.flashOff()
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
