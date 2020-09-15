package ink.z31.catbooru.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ink.z31.catbooru.R
import ink.z31.catbooru.ui.activity.SettingActivity
import ink.z31.catbooru.util.AppUtil
import ink.z31.catbooru.util.DateUtil
import kotlinx.android.synthetic.main.fragment_exception.*
import java.io.PrintWriter
import java.io.StringWriter

class ExceptionFragment(private val toolbar: Toolbar) : Fragment(), SettingActivity.ISettingFragment {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exception, container, false)
    }

    override fun getMenuRes(): Int? {
        return R.menu.exception_menu
    }

    override fun onStart() {
        super.onStart()
        view?.let {
            this.text_exception.text = errMsgBuilder.toString()
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.exception_copy -> {
                        val mClipData = ClipData.newPlainText("Label", errMsgBuilder)
                        val cm =
                            AppUtil.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.setPrimaryClip(mClipData)
                        Snackbar.make(it as View, "已复制到剪贴板", Snackbar.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
    }

    companion object {
        var errMsgBuilder: StringBuilder = StringBuilder()

        fun writeErrMsg(e: Exception, msg: String = "") {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            errMsgBuilder.apply {
                append("\r\n")
                append(DateUtil.timeStamp2Date(DateUtil.timeStamp()))
                append("\r\n")
                if (msg.isEmpty()) {
                    append(msg)
                    append("\r\n")
                }
                append(sw.toString())
                append("\r\n")
            }
        }
    }
}