import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.furkandursun.musicplayerapp.R
import com.furkandursun.musicplayerapp.model.Music

class MyFavoritesAdapter(private val musicList: List<Music>) :
    RecyclerView.Adapter<MyFavoritesAdapter.MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorites_item_list, parent, false)
        return MusicViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.bind(music)

        holder.itemView.setOnClickListener {

            val bundle = Bundle()
            bundle.putSerializable("music", music)
            Navigation.findNavController(holder.itemView)
                .navigate(R.id.action_myFavoritesFragment_to_songFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)



        fun bind(music: Music) {
            titleTextView.text = music.title
        }
    }
}
