package justbucket.ruherobase.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.AndroidInjection
import justbucket.ruherobase.R
import justbucket.ruherobase.domain.feature.log.GetAllLogs
import justbucket.ruherobase.domain.model.LogEntry
import kotlinx.android.synthetic.main.activity_log.*
import kotlinx.android.synthetic.main.content_log.*
import java.text.SimpleDateFormat
import javax.inject.Inject

class LogActivity : AppCompatActivity() {

    @Inject
    lateinit var getAllLogs: GetAllLogs

    companion object {
        fun newIntent(context: Context) = Intent(context, LogActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_log)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getAllLogs.execute({
            logRecycler.layoutManager = LinearLayoutManager(this)
            logRecycler.adapter = LogAdapter(it)
        })
    }

    private class LogAdapter(private val items: List<LogEntry>) : RecyclerView.Adapter<LogAdapter.LogHolder>() {

        private val params =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
            val view = TextView(parent.context)
            view.layoutParams = params
            view.gravity = Gravity.CENTER
            return LogHolder(view)
        }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: LogHolder, position: Int) {
            holder.bind(items[position])
        }

        class LogHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(log: LogEntry) {
                (itemView as TextView).text =
                    log.user.name + " on " + SimpleDateFormat.getDateTimeInstance().format(log.date) + " performed " + log.accessType.name + " on " + log.heroName
            }
        }
    }
}
