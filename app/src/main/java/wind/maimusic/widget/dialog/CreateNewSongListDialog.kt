package wind.maimusic.widget.dialog

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import wind.maimusic.R
import wind.maimusic.utils.getEditableStr
import wind.maimusic.utils.showKeyBoard
import wind.widget.utils.fastClickListener

class CreateNewSongListDialog(context: Context,private val dialogBtnClickListener: OnDialogBtnClickListener?=null):BaseDialog(context) {
    override fun layoutResId()=R.layout.view_create_song_list
    lateinit var etContent:EditText
    override fun initView() {
        super.initView()
        etContent = findViewById(R.id.dialog_et_song_list_title)
        val tvCancel:TextView  = findViewById(R.id.dialog_tv_song_list_cancel)
        val tvConfirm:TextView = findViewById(R.id.dialog_tv_song_list_confirm)
        tvConfirm.fastClickListener {
            dialogBtnClickListener?.onBtnClick(etContent.editableText.toString())
            dismiss()
        }
        tvCancel.setOnClickListener {
            dialogBtnClickListener?.onCancelClick()
            dismiss()
        }
    }

    override fun dismiss() {
        etContent.text ="".getEditableStr()
        super.dismiss()
    }
}