package justbucket.ruherobase.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import justbucket.ruherobase.R
import justbucket.ruherobase.domain.model.Hero
import kotlinx.android.synthetic.main.hero_item_holder.view.*

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class HeroItemAdapter(private val listener: (Hero) -> Unit) : RecyclerView.Adapter<HeroItemAdapter.HeroHolder>() {

    private val items: List<Hero> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hero_item_holder, parent, false)
        return HeroHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: HeroHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    class HeroHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hero: Hero, listener: (Hero) -> Unit) {
            Glide.with(itemView.context)
                    .load(hero.photoUrl)
                    .into(itemView.heroImage)
            itemView.heroTextName.text = hero.name
            itemView.heroTextOccupation.text = hero.occupation

            itemView.heroLayout.setOnClickListener { listener(hero) }
        }
    }
}