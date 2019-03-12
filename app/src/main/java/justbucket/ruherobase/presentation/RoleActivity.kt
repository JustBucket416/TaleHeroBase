package justbucket.ruherobase.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.AndroidInjection
import justbucket.ruherobase.R
import justbucket.ruherobase.domain.feature.role.AddRole
import justbucket.ruherobase.domain.feature.role.DeleteRole
import justbucket.ruherobase.domain.feature.role.GetAllRoles
import justbucket.ruherobase.domain.feature.role.UpdateRole
import justbucket.ruherobase.domain.model.AccessType
import justbucket.ruherobase.domain.model.Role
import kotlinx.android.synthetic.main.activity_role.*
import kotlinx.android.synthetic.main.add_role_dialog.*
import kotlinx.android.synthetic.main.content_role.*
import java.util.*
import javax.inject.Inject

class RoleActivity : AppCompatActivity() {

    @Inject
    lateinit var getAllRoles: GetAllRoles
    @Inject
    lateinit var addRole: AddRole
    @Inject
    lateinit var deleteRole: DeleteRole
    @Inject
    lateinit var updateRole: UpdateRole

    companion object {
        fun newIntent(context: Context) = Intent(context, RoleActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_role)
        setSupportActionBar(toolbar)

        ChooseUserActivity.user?.let {
            fab.visibility = if (it.accessTypeSet.contains(AccessType.CREATE)
                || it.roles?.any { it.accessTypes.contains(AccessType.CREATE) } == true
            ) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        fab.setOnClickListener {
            createAddRoleDialog()
                .setPositiveButton("Add") { dialog, _ ->
                    val name = (dialog as AlertDialog).editRoleName.text.toString()
                    val set = EnumSet.noneOf(AccessType::class.java)
                    if (dialog.checkCreate.isChecked) set.add(AccessType.CREATE)
                    if (dialog.checkRead.isChecked) set.add(AccessType.READ)
                    if (dialog.checkUpdate.isChecked) set.add(AccessType.UPDATE)
                    if (dialog.checkDelete.isChecked) set.add(AccessType.DELETE)
                    if (name.isNotBlank()) {
                        addRole.execute(params = Role(null, set, name), onResult = { getRoles() })
                    }
                    dialog.dismiss()
                }
                .show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getRoles()
    }

    private fun getRoles() {
        getAllRoles.execute({
            roleRecycler.layoutManager = LinearLayoutManager(this)
            if (ChooseUserActivity.user?.accessTypeSet?.contains(AccessType.UPDATE) == true
                || ChooseUserActivity.user?.roles?.any { it.accessTypes.contains(AccessType.UPDATE) } == true
            ) {
                roleRecycler.adapter = RoleAdapter(it) {
                    val dialog = createAddRoleDialog()
                        .setPositiveButton("Update") { dialog, _ ->
                            val name = (dialog as AlertDialog).editRoleName.text.toString()
                            val set = EnumSet.noneOf(AccessType::class.java)
                            if (dialog.checkCreate.isChecked) set.add(AccessType.CREATE)
                            if (dialog.checkRead.isChecked) set.add(AccessType.READ)
                            if (dialog.checkUpdate.isChecked) set.add(AccessType.UPDATE)
                            if (dialog.checkDelete.isChecked) set.add(AccessType.DELETE)
                            if (name.isNotBlank()) {
                                updateRole.execute(params = Role(it.id, set, name), onResult = { getRoles() })
                            }
                            dialog.dismiss()
                        }
                        .show()
                    dialog.editRoleName.setText(it.roleName)
                    if (it.accessTypes.contains(AccessType.CREATE)) dialog.checkCreate.isChecked = true
                    if (it.accessTypes.contains(AccessType.READ)) dialog.checkRead.isChecked = true
                    if (it.accessTypes.contains(AccessType.UPDATE)) dialog.checkUpdate.isChecked = true
                    if (it.accessTypes.contains(AccessType.DELETE)) dialog.checkDelete.isChecked = true
                }
            } else {
                roleRecycler.adapter = RoleAdapter(it, null)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_role, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_role -> {
                getAllRoles.execute({
                    if (it.isEmpty()) return@execute
                    createDeleteRoleDialog(HashMap(it.associate {
                        Pair(it, false)
                    })).show()
                })
                true
            }
            else -> true
        }
    }

    private fun createAddRoleDialog(): AlertDialog.Builder =
        AlertDialog.Builder(this)
            .setView(R.layout.add_role_dialog)
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

    private class RoleAdapter(
        private val itemList: List<Role>,
        private val onClick: ((Role) -> Unit)?
    ) : RecyclerView.Adapter<RoleAdapter.RoleHolder>() {

        private val params =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleHolder {
            val view = TextView(parent.context)
            view.layoutParams = params
            view.gravity = Gravity.CENTER
            return RoleHolder(view)
        }

        override fun getItemCount() = itemList.size

        override fun onBindViewHolder(holder: RoleHolder, position: Int) {
            holder.bind(itemList[position], onClick)
        }

        class RoleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(role: Role, onClick: ((Role) -> Unit)?) {
                (itemView as TextView).text = role.roleName
                itemView.setOnClickListener {
                    onClick?.invoke(role)
                }
            }
        }
    }

}