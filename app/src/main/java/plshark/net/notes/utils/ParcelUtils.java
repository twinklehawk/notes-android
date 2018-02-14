package plshark.net.notes.utils;

import android.os.Parcel;

/**
 * Utility methods for working with {@link Parcel}s
 */
public class ParcelUtils {

    private ParcelUtils() {

    }

    /**
     * Write a boolean. Use {@link #readBool(Parcel)} to read the boolean
     * @param b the boolean to write
     * @param parcel the parcel to write to
     */
    public static void write(boolean b, Parcel parcel) {
        parcel.writeByte(b ? (byte) 1 : (byte) 0);
    }

    /**
     * Read a boolean. Use {@link #write(boolean, Parcel)} to write the boolean
     * @param parcel the parcel to read from
     * @return the boolean value
     */
    public static boolean readBool(Parcel parcel) {
        return (parcel.readByte() == 1);
    }

    /**
     * Write a Long. Use {@link #readLong(Parcel)} to read the long
     * @param l the long to write
     * @param parcel the parcel to write to
     */
    public static void write(Long l, Parcel parcel) {
        if (l == null)
            write(false, parcel);
        else {
            write(true, parcel);
            parcel.writeLong(l);
        }
    }

    /**
     * Read a Long. Use {@link #write(Long, Parcel)} to write the Long
     * @param parcel the parcel to read from
     * @return the Long
     */
    public static Long readLong(Parcel parcel) {
        Long l = null;
        if (readBool(parcel))
            l = parcel.readLong();
        return l;
    }
}
