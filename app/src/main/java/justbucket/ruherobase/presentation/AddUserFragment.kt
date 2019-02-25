package justbucket.ruherobase.presentation


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import justbucket.ruherobase.R
import justbucket.ruherobase.domain.feature.role.GetAllRoles
import justbucket.ruherobase.domain.feature.user.AddUser
import justbucket.ruherobase.domain.model.*
import kotlinx.android.synthetic.main.fragment_add_user.*
import javax.inject.Inject

class AddUserFragment : DialogFragment() {

    @Inject
    lateinit var addUser: AddUser

    @Inject
    lateinit var getAllRoles: GetAllRoles

    lateinit var items: List<Role>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getAllRoles.execute({
            roleRecycler.layoutManager = LinearLayoutManager(context)
            roleRecycler.adapter = RoleAdapter(it)
            items = it
        })

        buttonCancel.setOnClickListener {
            dismiss()
        }

        buttonAdd.setOnClickListener {
            val name = editUserName.text.toString()
            if (name.isNotBlank()) {
                val roles = items.filter {
                    (roleRecycler.findViewHolderForItemId(it.id!!).itemView as CheckBox).isChecked
                }
                val user = when (userGroup.checkedRadioButtonId) {
                    R.id.radioUser -> SimpleUser(null, name, roles)
                    R.id.radioSuperUser -> SuperUser(null, name, roles)
                    R.id.radioModerator -> Moderator(null, name, roles)
                    R.id.radioAdmin -> Admin(null, name, roles)
                    else -> throw IllegalArgumentException()
                }
                addUser.execute({ dismiss() }, user)
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as MainActivity).getUsers()
    }

    private class RoleAdapter(private val items: List<Role>) : RecyclerView.Adapter<RoleAdapter.RoleHolder>() {

        init {
            setHasStableIds(true)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleHolder {
            return RoleHolder(LayoutInflater.from(parent.context).inflate(R.layout.role_holder, parent, false))
        }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: RoleHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemId(position: Int) = items[position].id!!

        private class RoleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(role: Role) {
                (itemView as CheckBox).text = role.roleName
            }
        }
    }
}
