package plshark.net.notes;

import android.os.Parcel;
import android.os.Parcelable;

import plshark.net.notes.utils.ParcelUtils;

/**
 * Data for a note
 */
public class Note implements Parcelable {

    private Long id;
    private Long serverId;
    private Long ownerId;
    private String title;
    private String content;

    /**
     * Create a new Note
     */
    public Note() {

    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            Note note = new Note();
            note.readFromParcel(source);
            return note;
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.write(id, dest);
        dest.writeString(title);
        dest.writeString(content);
    }

    /**
     * Read a note from a parcel
     * @param parcel the parcel to read from
     */
    void readFromParcel(Parcel parcel) {
        id = ParcelUtils.readLong(parcel);
        title = parcel.readString();
        content = parcel.readString();
    }
}
