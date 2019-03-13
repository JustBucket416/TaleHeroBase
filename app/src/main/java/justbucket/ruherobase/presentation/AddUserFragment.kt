package justbucket.ruherobase.presentation


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import justbucket.ruherobase.R
import justbucket.ruherobase.domain.feature.role.GetAllRoles
import justbucket.ruherobase.domain.feature.user.AddUser
import justbucket.ruherobase.domain.feature.user.UpdateUser
import justbucket.ruherobase.domain.model.*
import kotlinx.android.synthetic.main.fragment_add_user.*
import java.util.*
import javax.inject.Inject

class AddUserFragment : DialogFragment() {

    @Inject
    lateinit var addUser: AddUser
    @Inject
    lateinit var getAllRoles: GetAllRoles
    @Inject
    lateinit var updateUser: UpdateUser

    lateinit var items: List<Role>

    private var name: String? = null
    private var accessTypes: EnumSet<AccessType>? = null
    private var roles: List<Role>? = null
    private var id: Long? = null

    companion object {
        private const val NAME_KEY = "NameKey"
        private const val TYPES_KEY = "AcTypes"
        private const val ROLES_KEY = "RolesKey"
        private const val ID_KEY = "Idkey"

        fun newInstance(
                name: String? = null,
                accessTypes: EnumSet<AccessType>? = null,
                roles: ArrayList<Role>? = null,
                id: Long = -1
        ) = AddUserFragment().apply {
            if (name == null) return@apply
            arguments = Bundle().apply {
                putString(NAME_KEY, name)
                putSerializable(TYPES_KEY, accessTypes)
                putParcelableArrayList(ROLES_KEY, roles)
                putLong(ID_KEY, id)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        name = arguments?.getString(NAME_KEY)
        accessTypes = arguments?.getSerializable(TYPES_KEY) as? EnumSet<AccessType>?
        roles = arguments?.getParcelableArrayList(ROLES_KEY)
        id = arguments?.getLong(ID_KEY)
        if (id == -1L) {
            id = null
        }
        return inflater.inflate(R.layout.fragment_add_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getAllRoles.execute({
            roleRecycler.layoutManager = LinearLayoutManager(context)
            roleRecycler.adapter = RoleAdapter(it)
            items = it
            roleRecycler.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    roles?.forEach {
                        (roleRecycler.findViewHolderForItemId(it.id!!).itemView as CheckBox).isChecked = true
                    }
                    roleRecycler.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        })

        editUserName.setText(name)

        if (accessTypes != null) {
            when (accessTypes) {
                EnumSet.allOf(AccessType::class.java) -> userGroup.check(R.id.radioAdmin)
                EnumSet.of(AccessType.READ) -> userGroup.check(R.id.radioUser)
                EnumSet.of(AccessType.READ, AccessType.CREATE) -> userGroup.check(R.id.radioSuperUser)
                EnumSet.of(AccessType.UPDATE, AccessType.DELETE) -> userGroup.check(R.id.radioModerator)
                else -> throw IllegalArgumentException("unknown set")
            }
        }

        buttonCancel.setOnClickListener {
            dismiss()
        }

        if (id != null) {
            buttonAdd.text = getString(R.string.update)
        }

        buttonAdd.setOnClickListener {
            val name = editUserName.text.toString()
            if (name.isNotBlank()) {
                val roles = items.filter {
                    (roleRecycler.findViewHolderForItemId(it.id!!).itemView as CheckBox).isChecked
                }
                val user = when (userGroup.checkedRadioButtonId) {
                    R.id.radioUser -> SimpleUser(id, name, roles)
                    R.id.radioSuperUser -> SuperUser(id, name, roles)
                    R.id.radioModerator -> Moderator(id, name, roles)
                    R.id.radioAdmin -> Admin(id, name, roles)
                    else -> throw IllegalArgumentException()
                }
                if (accessTypes == null) {
                    addUser.execute({ dismiss() }, user)
                } else {
                    updateUser.execute({ dismiss() }, user)
                }
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as ChooseUserActivity).getUsers()
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
