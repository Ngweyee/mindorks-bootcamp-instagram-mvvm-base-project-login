package com.mindorks.bootcamp.instagram.utils.display

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.mindorks.bootcamp.instagram.R
import kotlinx.android.synthetic.main.loading_view.view.*

class CustomProgressBar(context: Context,attributeSet: AttributeSet) : MaterialCardView(context,attributeSet){


    lateinit var dialog: Dialog
    fun show(context: Context): Dialog {
        return show(context, null)
    }

    fun show(context: Context, title: CharSequence?): Dialog {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout.loading_view, null)
        if (title != null) {
            view.tvProgressName.text = title
        }

        view.tvProgressName.setTextColor(Color.BLACK) //Text Color
        dialog = Dialog(context, R.style.ThemeOverlay_AppCompat_Dialog)
        dialog.setContentView(view)
        dialog.show()
        return dialog
    }

    fun hide(){
        dialog.run {
           if(this.isShowing )
               Thread.sleep(2000)
               dialog.dismiss()
        }
    }

}
