package de.reiss.edizioni.note.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.edizioni.R
import de.reiss.edizioni.architecture.AppActivity
import de.reiss.edizioni.model.TheWordContent
import de.reiss.edizioni.util.extensions.findFragmentIn
import de.reiss.edizioni.util.extensions.replaceFragmentIn
import de.reiss.edizioni.util.extensions.withZeroDayTime
import kotlinx.android.synthetic.main.edit_note_activity.*
import java.util.*

class EditNoteActivity : AppActivity() {

    companion object {

        private const val KEY_TIME = "KEY_TIME"
        private const val KEY_THE_WORD_CONTENT = "KEY_THE_WORD_CONTENT"

        fun createIntent(context: Context, date: Date, theWordContent: TheWordContent): Intent =
                Intent(context, EditNoteActivity::class.java)
                        .putExtra(KEY_TIME, date.withZeroDayTime().time)
                        .putExtra(KEY_THE_WORD_CONTENT, theWordContent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_note_activity)
        setSupportActionBar(edit_note_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (findFragmentIn(R.id.edit_note_fragment_container) == null) {
            val time = intent.getLongExtra(KEY_TIME, -1L)
            if (time == -1L) {
                throw IllegalStateException("No time given for note")
            }
            val theWordContent = intent.getParcelableExtra<TheWordContent>(KEY_THE_WORD_CONTENT)
                    ?: throw IllegalStateException("No word given for note")

            replaceFragmentIn(
                    container = R.id.edit_note_fragment_container,
                    fragment = EditNoteFragment.createInstance(time, theWordContent))
        }
    }

}