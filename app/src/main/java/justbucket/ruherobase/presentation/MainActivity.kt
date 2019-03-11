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
import justbucket.ruherobase.presentation.ChooseUserActivity.Companion.user
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
    lateinit var getAllHeroes: GetAllHeroes
    @Inject
    lateinit var addHero: AddHero
    @Inject
    lateinit var deleteHero: DeleteHero
    @Inject
    lateinit var addRole: AddRole
    @Inject
    lateinit var deleteRole: DeleteRole

    private val adapter = HeroItemAdapter {
        startActivity(DetailActivity.newIntent(this,
                it, ChooseUserActivity.user?.accessTypeSet?.contains(AccessType.UPDATE) == true
                || ChooseUserActivity.user?.roles?.any { it.accessTypes.contains(AccessType.UPDATE) } == true))
    }
    private val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            deleteHero.execute(params = adapter.getHeroFromHolder(viewHolder))
        }
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        AndroidInjection.inject(this)
    }

    override fun onStart() {
        super.onStart()
        initRecycler()
        setupDelete()
        initFab()
        getAllHeroes.execute({ adapter.updateList(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_users -> startActivity(ChooseUserActivity.newIntent(this))
        }
        return true
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupDelete() {
        if (ChooseUserActivity.user?.accessTypeSet?.contains(AccessType.DELETE) == true
                || ChooseUserActivity.user?.roles?.any { it.accessTypes.contains(AccessType.DELETE) } == true
        ) {
            helper.attachToRecyclerView(recyclerView)
        } else {
            helper.attachToRecyclerView(null)
        }
    }

    private fun initFab() {
        if (user?.accessTypeSet?.contains(AccessType.CREATE) == true
                || user?.roles?.any { it.accessTypes.contains(AccessType.CREATE) } == true
        ) {
            fab.visibility = View.VISIBLE
            fab.setOnClickListener {
                createAddHeroDialog().show()
            }
        } else {
            fab.visibility = View.GONE
        }
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

}
