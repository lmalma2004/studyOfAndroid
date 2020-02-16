package com.example.todolist

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

class TodoListAdapter(realmResult: OrderedRealmCollection<Todo>)
    : RealmBaseAdapter<Todo>(realmResult){
    //매 아이템이 화면에 보일 때마다 호출됨
    //convertView는 아이템이 작성되기 전에는 null이고 한 번 작성되면 이전에 작성했던 뷰를 전달
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val vh: ViewHolder
        val view: View

        if(convertView == null){
            //LayoutInflater 클래스는 XML 레이아웃 파일을 코드로 불러오는 기능을 제공
            //LayoutInflater.from(parent?.context) 메서드로 객체를 얻고
            //inflate() 메서드로 XML 레이아웃 파일을 읽어서 뷰로 반환하여 view 변수에 할당
            //inflate(resource : Int, root: ViewGroup, attachToRoot: Boolean)
            //resource : 불러올 레이아웃 XML 리소스 ID를 지정
            //root : 불러온 레이아웃 파일이 붙을 뷰그룹인 parent를 지정
            //attachToRoot : XML 파일을 불러올 때는 false를 지정
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_todo, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else{
            view = convertView
            vh = view.tag as ViewHolder
        }

        if(adapterData != null){
            val item = adapterData!![position]
            vh.textTextView.text = item.title
            vh.dateTextView.text = DateFormat.format("yyyy/MM/dd", item.date)
        }

        return view
    }

    override fun getItemId(position: Int): Long {
        if(adapterData != null)
            return adapterData!![position].id
        return super.getItemId(position)
    }
}

class ViewHolder(view: View){
    val dateTextView: TextView = view.findViewById(R.id.text1)
    val textTextView: TextView = view.findViewById(R.id.text2)
}