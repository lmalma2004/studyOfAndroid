package com.example.mygallery

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaActionSound
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private val REQUEST_READ_EXTERNAL_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            //권한이 허용되지 않음
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                //이전에 이미 권한이 거부되었을 때 설명
                //shouldShowRequestPermissionRationale() 메서드는 사용자가 전에 권한 요청을 거부했는지를 반환한다.
                //true를 반환하면 거부를 한 적이 있는 것
                alert("사진 정보를 얻으려면 외부 저장소 권한이 필수로 필요합니다",
                        "권한이 필요한 이유"){
                    yesButton {
                        //권한 요청
                        ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                REQUEST_READ_EXTERNAL_STORAGE)
                    }
                    noButton {  }
                }.show()
            }
            else{
                //권한 요청
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_EXTERNAL_STORAGE)
            }
        }
        else{
            //권한이 이미 허용됨
            getAllPhotos()
        }
    }
    private fun getAllPhotos(){
        //모든 사진 정보 가져오기
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, // 가져올 항목 배열, 모르겠으면 null -> 모든항목을 가져옴
                null,  // 조건
                null, // 조건
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")

        val fragments = ArrayList<Fragment>()
        if(cursor != null){
            val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            while(cursor.moveToNext()){
                //사진 경로 Uri 가져오기
                //val columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                //val imageId = cursor.getLong(columnIndexId)
                //val uriImage = Uri.withAppendedPath(uriExternal, "" + imageId)
                //Log.d("MainActivity", uriImage.toString())
                val uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                Log.d("MainActivity", uri)
                fragments.add(PhotoFragment.newInstance(uri))
            }
            cursor.close()
        }

        val adapter = MyPagerAdapter(supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        adapter.updateFragments(fragments)
        viewPager.adapter = adapter

        timer(period = 3000){
            runOnUiThread {
                if(viewPager.currentItem < adapter.count - 1){
                    viewPager.currentItem = viewPager.currentItem + 1
                }
                else{
                    viewPager.currentItem = 0
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //grandResults 배열에는 요청한 권한들의 결과가 전달됨
        //지금은 하나의 권한만 요청했기 때문에 0번 인덱스값만 확인
        when(requestCode){
            REQUEST_READ_EXTERNAL_STORAGE->{
                if((grantResults.isNotEmpty()
                                && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    getAllPhotos()
                }
                else{
                    toast("권한 거부 됨")
                }
                return
            }
        }
    }
}
