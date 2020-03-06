package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email
import org.jetbrains.anko.sendSMS
import org.jetbrains.anko.share

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //웹뷰를 사용할 때 항상 기본으로 두 가지 설정을 해야 한다.
        //첫째, javaScriptEnabled 기능을 켠다. 그래야 자바스크립트 기능이 잘 동작한다.
        //둘째, webViewClient는 WebViewClient 클래스를 지정하는데 이것을 지정하지 않으면 웹뷰에 페이지가 표시되지 않고
        //자체 웹브라우저가 동작한다.
        webView.apply{
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }

        webView.loadUrl("http://www.google.com")

        //setOnEditorActionListener는 에디트텍스트가 선택되고 글자가 입력될 때마다 호출된다.
        //인자로는 반응한 뷰, 액션ID, 이벤트 세 가지며, 여기서는 뷰와 이벤트를 사용하지 않기 때문에 _로 대치할 수 있다.
        urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                webView.loadUrl(urlEditText.text.toString())
                true
            }
            else{
                false
            }
        }

        //컨텍스트 메뉴 등록
        registerForContextMenu(webView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_google, R.id.action_home -> {
                webView.loadUrl("http://www.google.com")
                return true
            }
            R.id.action_naver -> {
                webView.loadUrl("http://www.naver.com")
                return true
            }
            R.id.action_daum -> {
                webView.loadUrl("http://www.daum.net")
                return true
            }
            R.id.action_call -> {
                val intent = Intent(Intent.ACTION_DIAL) // ACTION_DIAL은 전화 다이얼을 입력해주는 액션
                //인텐트에 데이터를 지정
                //'tel:'로 시작하는 Uri는 전화번호를 나타내는 국제표준 방법이다.
                intent.data = Uri.parse("tel:010-2859-7704")
                //intent.resolveActivity() 메서드는 이 인텐트를 수행하는 액티비티가 있는지를 검사하여 반환한다.
                //null이 반환된다면 수행하는 액티비티가 없는 것
                if(intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {
                sendSMS("010-2859-7704", webView.url)
                return true
            }
            R.id.action_email -> {
                email("lmalma2004@naver.com", "좋은 사이트", webView.url)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_share -> {
                share(webView.url)
            }
            R.id.action_browser -> {
                browse(webView.url)
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }
        else{
            super.onBackPressed()
        }
    }
}
