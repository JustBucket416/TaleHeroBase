package justbucket.ruherobase.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dagger.android.AndroidInjection
import justbucket.ruherobase.R
import justbucket.ruherobase.domain.feature.hero.UpdateHero
import justbucket.ruherobase.domain.model.AccessType
import justbucket.ruherobase.domain.model.Hero
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    @Inject
    lateinit var updateHero: UpdateHero

    private lateinit var hero: Hero

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        AndroidInjection.inject(this)

        with(intent.getParcelableExtra<Hero>(KEY)) {
            hero = this
            editHeroName.setText(name)
            editNumber.setText(mentionNumber.toString())
            editDescription.setText(description)
            editOccupation.setText(occupation)
            Glide.with(this@DetailActivity)
                    .load(photoUrl)
                    .into(imageDetail)
            imageDetail.contentDescription = photoUrl
        }

        fab.setOnClickListener { view ->
            val name = editHeroName.text.toString()
            val number = editNumber.text.toString()
            val description = editDescription.text.toString()
            val occupation = editOccupation.text.toString()
            val photoUrl = imageDetail.contentDescription.toString()
            if (name.isNotBlank()
                    && number.isNotBlank()
                    && description.isNotBlank()
                    && occupation.isNotBlank()
                    && photoUrl.isNotBlank()
            ) {
                updateHero.execute(
                        params = Hero(
                                hero.id,
                                name,
                                Integer.parseInt(number),
                                description,
                                occupation,
                                photoUrl
                        )
                )
            }
        }

        if (MainActivity.user?.accessTypeSet?.contains(AccessType.UPDATE) == true
                || MainActivity.user?.roles?.any { it.accessTypes.contains(AccessType.UPDATE) } == true
        ) {
            imageDetail.setOnClickListener {
                createInputDialogBox((EditText(this))).show()
            }
        } else {
            fab.visibility = View.GONE
            editHeroName.isFocusable = false
            editNumber.isFocusable = false
            editDescription.isFocusable = false
            editOccupation.isFocusable = false
        }
    }

    private fun createInputDialogBox(text: EditText): AlertDialog.Builder =
            AlertDialog.Builder(this)
                    .setTitle("Update Image")
                    .setView(text)
                    .setPositiveButton("Update") { dialog, _ ->
                        imageDetail.contentDescription = text.text.toString()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }

    companion object {
        private const val KEY = "key"
        private const val EDIT_KEY = "edit-key"

        fun newIntent(context: Context, hero: Hero, canEdit: Boolean) =
                Intent(context, DetailActivity::class.java).apply {
                    putExtra(KEY, hero)
                    putExtra(EDIT_KEY, canEdit)
                }
    }

}
