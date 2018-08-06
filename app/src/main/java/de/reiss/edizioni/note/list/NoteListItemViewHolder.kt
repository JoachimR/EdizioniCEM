package  de.reiss.edizioni.note.list

import android.view.View
import android.widget.TextView
import de.reiss.edizioni.R
import de.reiss.edizioni.formattedDate
import de.reiss.edizioni.util.extensions.onClick
import de.reiss.edizioni.util.view.ListItemViewHolder
import de.reiss.edizioni.util.view.StableListItem

class NoteListItemViewHolder(layout: View,
                             private val noteClickListener: NoteClickListener)
    : ListItemViewHolder(layout) {

    private val context = layout.context
    private val noteDate = layout.findViewById<TextView>(R.id.note_list_item_date)
    private val noteText = layout.findViewById<TextView>(R.id.note_list_item_text)

    private var item: NoteListItem? = null

    init {
        layout.onClick {
            item?.let {
                noteClickListener.onNoteClicked(it.note)
            }
        }
    }

    override fun bindViews(item: StableListItem, isLastItem: Boolean) {
        if (item is NoteListItem) {
            this.item = item
            noteDate.text = formattedDate(context, item.note.date.time)
            noteText.text = item.note.noteText
        }
    }

}