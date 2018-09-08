package de.reiss.edizioni.database.items.text;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import static de.reiss.edizioni.database.items.text.ConstantsKt.text_item_id;

public class TextItemFull {
    @Embedded
    public TextItem textItem;

    @Relation(parentColumn = "id", entityColumn = text_item_id)
    public List<DevotionItem> devotionItems;

    @Override
    public String toString() {
        return "TextItemFull{" +
                "textItem=" + textItem +
                ", devotionItems=" + devotionItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextItemFull that = (TextItemFull) o;

        if (textItem != null ? !textItem.equals(that.textItem) : that.textItem != null)
            return false;
        return devotionItems != null ? devotionItems.equals(that.devotionItems) : that.devotionItems == null;
    }

    @Override
    public int hashCode() {
        int result = textItem != null ? textItem.hashCode() : 0;
        result = 31 * result + (devotionItems != null ? devotionItems.hashCode() : 0);
        return result;
    }
}
