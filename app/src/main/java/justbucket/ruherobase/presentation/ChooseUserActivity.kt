package justbucket.ruherobase.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import justbucket.ruherobase.R
import justbucket.ruherobase.domain.feature.user.DeleteUser
import justbucket.ruherobase.domain.feature.user.GetAllUsers
import justbucket.ruherobase.domain.model.AccessType
import justbucket.ruherobase.domain.model.Role
import justbucket.ruherobase.domain.model.User
import kotlinx.android.synthetic.main.activity_choose_user.*
import kotlinx.android.synthetic.main.content_choose_user.*
import javax.inject.Inject

class ChooseUserActivity : AppCompatActivity() {

    @Inject
    lateinit var getAllUsers: GetAllUsers
    @Inject
    lateinit var deleteUser: DeleteUser

    private val users = mutableListOf<User>()

    companion object {
        var user: User? = null

        fun newIntent(context: Context) = Intent(context, ChooseUserActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val fragment = AddUserFragment.newInstance(null, null, null)
            fragment.show(supportFragmentManager, "add-user")
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getUsers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_choose_user, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_user -> deleteUsers()
            else -> true
        }
    }

    fun getUsers() {
        getAllUsers.execute({
            users.clear()
            users.addAll(it)
            if (user == null) {
                user = users.find { it.id == 0L }
            }
            updateUserList()
        })
    }

    private fun updateFabVisibility() {
        fab.visibility = if (user?.accessTypeSet?.contains(AccessType.CREATE) == true
            || user?.roles?.any { it.accessTypes.contains(AccessType.CREATE) } == true
        ) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun updateUserList() {
        userGroup.removeAllViews()
        users.forEach { curUser ->
            userGroup.addView(
                RadioButton(this).apply {
                    text = curUser.name
                    if (curUser.id == user?.id) {
                        tag = "checked"
                    }
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            user = curUser
                            updateFabVisibility()
                        }
                    }
                    setOnLongClickListener {
                        if (user?.accessTypeSet?.contains(AccessType.UPDATE) == true
                            || user?.roles?.any { it.accessTypes.contains(AccessType.UPDATE) } == true
                        ) {
                            AddUserFragment.newInstance(
                                curUser.name, curUser.accessTypeSet,
                                curUser.roles as? java.util.ArrayList<Role>, curUser.id!!
                            ).show(supportFragmentManager, "update")
                        }
                        true
                    }
                }
            )
        }
        userGroup.check(userGroup.findViewWithTag<RadioButton>("checked").id)
    }

    private fun deleteUsers(): Boolean {
        val userList = ArrayList(users).apply {
            remove(user)
        }.filter { it.id != 0L }
        if (userList.isEmpty()) return true
        val users = HashMap(userList.associate { Pair(it, false) })
        createDeleteUserDialog(users).show()
        return true
    }

    private fun createDeleteUserDialog(users: HashMap<User, Boolean>): AlertDialog.Builder =
        AlertDialog.Builder(this)
            .setMultiChoiceItems(users.keys.map { it.name }.toTypedArray(), null)
            { _, which, isChecked ->
                users[users.keys.toList()[which]] = isChecked
            }
            .setPositiveButton("Delete") { dialog, _ ->
                users.filter { it.value }.forEach { t, _ -> deleteUser.execute(params = t) }
                getUsers()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
}
