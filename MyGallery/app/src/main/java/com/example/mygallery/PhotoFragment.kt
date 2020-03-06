package com.example.mygallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_photo.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_URI = "uri"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var uri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //프래그먼트가 생성되면 onCreate()메서드가 호출되고 ARG_URI 키에 저장된 uri값을 얻어서 변수에 저장
        arguments?.let {
            uri = it.getString(ARG_URI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //액티비티가 아닌 곳에서 레이아웃 리소스를 가지고 오려면 LayoutInflater객체의 inflate() 메서드를 사용
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    //뷰가 완성된 직후에 호출되는 onViewCreated() 메서드를 오버라이드 하고 Glide 라이브러리로 사진을 이미지뷰에 표시
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this).load(uri).into(imageView)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhotoFragment.
         */
        // TODO: Rename and change types and number of parameters

        //newInstance()메서드를 이용하여 프래그먼트를 생성할 수 있고 인자로 uri값을 전달한다
        //이 값은 Bundle 객체에 ARG_URI 키로 저장되고 arguments 프로퍼티에 저장된다.
        @JvmStatic
        fun newInstance(uri: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URI, uri)
                }
            }
    }
}
