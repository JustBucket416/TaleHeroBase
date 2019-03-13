package justbucket.ruherobase.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import justbucket.ruherobase.data.database.HeroDatabase
import justbucket.ruherobase.domain.feature.hero.AddHero
import justbucket.ruherobase.domain.feature.hero.DeleteHero
import justbucket.ruherobase.domain.feature.hero.GetAllHeroes
import justbucket.ruherobase.domain.model.AccessType
import justbucket.ruherobase.domain.model.Hero
import justbucket.ruherobase.presentation.ChooseUserActivity.Companion.user
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_hero_dialog.*
import kotlinx.android.synthetic.main.content_main.*
import org.apache.commons.io.FileUtils
import java.io.File
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var getAllHeroes: GetAllHeroes
    @Inject
    lateinit var addHero: AddHero
    @Inject
    lateinit var deleteHero: DeleteHero

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
            deleteHero.execute(
                params = DeleteHero.Params.createParams(
                    adapter.getHeroFromHolder(viewHolder),
                    ChooseUserActivity.user?.id!!
                )
            )
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
        //ChooseUserActivity.user = Admin(0, "Admin", emptyList())
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
            R.id.action_roles -> startActivity(RoleActivity.newIntent(this))
            R.id.action_logs -> startActivity(LogActivity.newIntent(this))
            R.id.action_backup -> checkPermissions()
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showBackupDialog()
        }
    }

    private fun checkPermissions() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        } else {
            showBackupDialog()
        }
    }

    private fun showBackupDialog() {
        AlertDialog.Builder(this)
                .setTitle("Choose action")
                .setPositiveButton("Backup") { dialog, _ ->
                    backupDB()
                    dialog.dismiss()
                }
                .setNegativeButton("Restore") { dialog, _ ->
                    loadDB()
                    dialog.dismiss()
                }
                .show()
    }

    private fun backupDB() {
        HeroDatabase.close(this)
        val db = getDatabasePath("heroes.db")
        val dbShm = File(db.parent, "heroes.db-shm")
        val dbWal = File(db.parent, "heroes.db-wal")

        val db2 = File(Environment.getExternalStorageDirectory(), "heroes.db")
        val dbShm2 = File(db2.parent, "heroes.db-shm")
        val dbWal2 = File(db2.parent, "heroes.db-wal")

        try {
            FileUtils.copyFile(db, db2)
            FileUtils.copyFile(dbShm, dbShm2)
            FileUtils.copyFile(dbWal, dbWal2)
        } catch (e: Exception) {
            Log.e("SAVEDB", e.toString())
        }
    }

    private fun loadDB() {
        HeroDatabase.close(this)
        val db = File(Environment.getExternalStorageDirectory(), "heroes.db")
        val dbShm = File(db.parent, "heroes.db-shm")
        val dbWal = File(db.parent, "heroes.db-wal")

        val db2 = getDatabasePath("heroes.db")
        val dbShm2 = File(db2.parent, "heroes.db-shm")
        val dbWal2 = File(db2.parent, "heroes.db-wal")

        try {
            FileUtils.copyFile(db, db2)
            FileUtils.copyFile(dbShm, dbShm2)
            FileUtils.copyFile(dbWal, dbWal2)
        } catch (e: Exception) {
            Log.e("RESTOREDB", e.toString())
        }

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
                        AddHero.Params.createParams(
                            Hero(
                                -1,
                                name,
                                Integer.parseInt(number),
                                description,
                                occupation,
                                url
                            ), ChooseUserActivity.user?.id!!
                        )
                    )
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

}
