package justbucket.ruherobase.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import justbucket.ruherobase.domain.feature.user.DeleteUser
import justbucket.ruherobase.domain.feature.user.GetAllUsers
import justbucket.ruherobase.domain.model.AccessType
import justbucket.ruherobase.domain.model.Hero
import justbucket.ruherobase.domain.model.Role
import justbucket.ruherobase.domain.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_hero_dialog.*
import kotlinx.android.synthetic.main.add_role_dialog.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

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
    lateinit var deleteHero: DeleteHero
    @Inject
    lateinit var addRole: AddRole
    @Inject
    lateinit var deleteRole: DeleteRole
    @Inject
    lateinit var deleteUser: DeleteUser

    companion object {
        var user: User? = null
    }

    private val users = mutableListOf<User>()
    private val adapter = HeroItemAdapter {
        startActivity(DetailActivity.newIntent(this,
                it, user?.accessTypeSet?.contains(AccessType.UPDATE) == true
                || user?.roles?.any { it.accessTypes.contains(AccessType.UPDATE) } == true))
    }
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
    private var groupId = 0


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
        groupId = (if (users.isNotEmpty()) users[users.size - 1].id!! + 1L else 0).toInt()
        val sub = menu.addSubMenu(0, groupId, Menu.CATEGORY_SECONDARY, "Users")
        users.forEach {
            sub.add(0, it.id!!.toInt(), Menu.NONE, it.name)
        }
        val bool = user?.accessTypeSet?.contains(AccessType.DELETE) == true
                || user?.roles?.any { it.accessTypes.contains(AccessType.DELETE) } == true
        menu.findItem(R.id.action_delete_role).isEnabled = bool
        menu.findItem(R.id.action_delete_user).isEnabled = bool
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
                true
            }
            R.id.action_add_role -> {
                createAddRoleDialog().show()
                true
            }
            R.id.action_delete_role -> {
                getAllRoles.execute({
                    if (it.isEmpty()) return@execute
                    createDeleteRoleDialog(HashMap(it.associate {
                        Pair(it, false)
                    })).show()
                })
                true
            }
            R.id.action_delete_user -> {
                val userList = ArrayList(users).apply { remove(user) }
                if (userList.isEmpty()) return true
                val users = HashMap(userList.associate { Pair(it, false) })
                createDeleteUserDialog(users).show()
                true
            }
            groupId -> true
            else -> {
                user = users.filter { it.id!!.toInt() == item.itemId }[0]
                initFab()
                setupDelete()
                invalidateOptionsMenu()
                true
            }
        }
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupDelete() {
        if (user?.accessTypeSet?.contains(AccessType.DELETE) == true
                || user?.roles?.any { it.accessTypes.contains(AccessType.DELETE) } == true) {
            helper.attachToRecyclerView(recyclerView)
        } else {
            helper.attachToRecyclerView(null)
        }
    }

    private fun initFab() {
        if (user?.accessTypeSet?.contains(AccessType.CREATE) == true
                || user?.roles?.any { it.accessTypes.contains(AccessType.CREATE) } == true) {
            fab.visibility = View.VISIBLE
            fab.setOnClickListener {
                createAddHeroDialog().show()
            }
        } else {
            fab.visibility = View.GONE
        }
    }

    fun getUsers() {
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
                        val name = (dialog as AlertDialog).editHeroName.text.toString()
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


    private fun createAddRoleDialog(): AlertDialog.Builder =
            AlertDialog.Builder(this)
                    .setView(R.layout.add_role_dialog)
                    .setPositiveButton("Add") { dialog, _ ->
                        val name = (dialog as AlertDialog).editRoleName.text.toString()
                        val set = EnumSet.noneOf(AccessType::class.java)
                        if (dialog.checkCreate.isChecked) set.add(AccessType.CREATE)
                        if (dialog.checkRead.isChecked) set.add(AccessType.READ)
                        if (dialog.checkUpdate.isChecked) set.add(AccessType.UPDATE)
                        if (dialog.checkDelete.isChecked) set.add(AccessType.DELETE)
                        if (name.isNotBlank()) {
                            addRole.execute(params = Role(null, set, name))
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }

    private fun createDeleteRoleDialog(roles: HashMap<Role, Boolean>): AlertDialog.Builder =
            AlertDialog.Builder(this)
                    .setMultiChoiceItems(roles.keys.map { it.roleName }.toTypedArray(), null)
                    { _, which, isChecked ->
                        roles[roles.keys.toList()[which]] = isChecked
                    }
                    .setPositiveButton("Delete") { dialog, _ ->
                        roles.filter { it.value }.forEach { t, _ -> deleteRole.execute(params = t) }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }

    private fun createDeleteUserDialog(users: HashMap<User, Boolean>): AlertDialog.Builder =
            AlertDialog.Builder(this)
                    .setMultiChoiceItems(users.keys.map { it.name }.toTypedArray(), null)
                    { _, which, isChecked ->
                        users[users.keys.toList()[which]] = isChecked
                    }
                    .setPositiveButton("Delete") { dialog, _ ->
                        users.filter { it.value }.forEach { t, u -> deleteUser.execute(params = t) }
                        getUsers()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }

}
