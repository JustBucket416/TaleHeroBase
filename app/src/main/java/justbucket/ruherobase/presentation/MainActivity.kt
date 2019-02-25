package justbucket.ruherobase.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.AndroidInjection
import justbucket.ruherobase.R
import justbucket.ruherobase.domain.feature.hero.AddHero
import justbucket.ruherobase.domain.feature.hero.DeleteHero
import justbucket.ruherobase.domain.feature.hero.GetAllHeroes
import justbucket.ruherobase.domain.feature.role.AddRole
import justbucket.ruherobase.domain.feature.role.DeleteRole
import justbucket.ruherobase.domain.feature.role.GetAllRoles
import justbucket.ruherobase.domain.feature.user.AddUser
import justbucket.ruherobase.domain.feature.user.DeleteUser
import justbucket.ruherobase.domain.feature.user.GetAllUsers
import justbucket.ruherobase.domain.model.AccessType
import justbucket.ruherobase.domain.model.Hero
import justbucket.ruherobase.domain.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_hero_dialog.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var getAllRoles: GetAllRoles
    @Inject
    lateinit var getAllUsers: GetAllUsers
    @Inject
    lateinit var getAllHeroes: GetAllHeroes
    @Inject
    lateinit var addHero: AddHero
    @Inject
    lateinit var addUser: AddUser
    @Inject
    lateinit var deleteHero: DeleteHero
    @Inject
    lateinit var addRole: AddRole
    @Inject
    lateinit var deleteRole: DeleteRole
    @Inject
    lateinit var deleteUser: DeleteUser

    companion object {
        lateinit var user: User
    }

    private val users = mutableListOf<User>()
    private val adapter = HeroItemAdapter { startActivity(DetailActivity.newIntent(this, it)) }
    private val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            deleteHero.execute({ getUsers() }, adapter.getHeroFromHolder(viewHolder))
        }
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        AndroidInjection.inject(this)

        getUsers()
        initRecycler()
        getAllHeroes.execute({ adapter.updateList(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        users.forEach {
            menu.add(0, it.id!!.toInt(), Menu.NONE, it.name)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_add_user -> {
                val userDialog = AddUserFragment()
                userDialog.show(supportFragmentManager, "add-user")
                userDialog.dialog?.setOnDismissListener {
                    getUsers()
                    it.dismiss()
                }
                true
            }
            else -> {
                user = users.filter { it.id!!.toInt() == item.itemId }[0]
                initFab()
                setupDelete()
                true
            }
        }
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupDelete() {
        if (user.accessTypeSet.contains(AccessType.DELETE)) {
            helper.attachToRecyclerView(recyclerView)
        } else {
            helper.attachToRecyclerView(null)
        }
    }

    private fun initFab() {
        if (user.accessTypeSet.contains(AccessType.CREATE)) {
            fab.visibility = View.VISIBLE
            fab.setOnClickListener {
                createAddHeroDialog().show()
            }
        } else {
            fab.visibility = View.GONE
        }
    }

    private fun getUsers() {
        getAllUsers.execute({
            users.clear()
            users.addAll(it)
            invalidateOptionsMenu()
        })
    }

    private fun getHeroes() {
        getAllHeroes.execute({
            adapter.updateList(it)
        })
    }

    private fun createAddHeroDialog(): AlertDialog.Builder =
            AlertDialog.Builder(this)
                    .setView(R.layout.add_hero_dialog)
                    .setPositiveButton("Add") { dialog, _ ->
                        val name = (dialog as AlertDialog).editName.text.toString()
                        val number = dialog.editNumber.text.toString()
                        val occupation = dialog.editOccupation.text.toString()
                        val description = dialog.editDescription.text.toString()
                        val url = dialog.editImageUrl.text.toString()
                        if (name.isNotBlank() &&
                                number.isNotBlank() &&
                                occupation.isNotBlank() &&
                                description.isNotBlank() &&
                                url.isNotBlank()
                        ) {
                            addHero.execute(
                                    { getHeroes() },
                                    Hero(-1, name, Integer.parseInt(number), description, occupation, url)
                            )
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }


    private fun createInputBox(context: Context, hint: String) =
            EditText(context).apply {
                this.hint = hint
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }

}
