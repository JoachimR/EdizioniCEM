package de.reiss.edizioni.database.items.meta;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import static de.reiss.edizioni.database.items.meta.ConstantsKt.meta_item_id;

public class MetaItemFull {
    @Embedded
    public MetaItem metaItem;

    @Relation(parentColumn = "id", entityColumn = meta_item_id)
    public List<AuthorItem> authorItems;

    @Relation(parentColumn = "id", entityColumn = meta_item_id)
    public List<InfoItem> infoItems;

    @Override
    public String toString() {
        return "MetaItemFull{" +
                "metaItem=" + metaItem +
                ", authorItems=" + authorItems +
                ", infoItems=" + infoItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetaItemFull that = (MetaItemFull) o;

        if (metaItem != null ? !metaItem.equals(that.metaItem) : that.metaItem != null)
            return false;
        if (authorItems != null ? !authorItems.equals(that.authorItems) : that.authorItems != null)
            return false;
        return infoItems != null ? infoItems.equals(that.infoItems) : that.infoItems == null;
    }

    @Override
    public int hashCode() {
        int result = metaItem != null ? metaItem.hashCode() : 0;
        result = 31 * result + (authorItems != null ? authorItems.hashCode() : 0);
        result = 31 * result + (infoItems != null ? infoItems.hashCode() : 0);
        return result;
    }
}
