package plshark.net.notes.utils;

import android.os.Parcel;

/**
 * Created by alex on 11/19/16.
 */

public class ParcelUtils {
    /**
     * Create a new ParcelUtils object
     * <p>
     * This class should not be instantiated
     * </p>
     */
    private ParcelUtils() {

    }

    public static void write(boolean b, Parcel parcel) {
        parcel.writeByte(b ? (byte) 1 : (byte) 0);
    }

    public static boolean readBool(Parcel parcel) {
        return (parcel.readByte() == 1);
    }

    public static void write(Long l, Parcel parcel) {
        if (l == null)
            write(false, parcel);
        else {
            write(true, parcel);
            parcel.writeLong(l);
        }
    }

    public static Long readLong(Parcel parcel) {
        Long l = null;
        if (readBool(parcel))
            l = parcel.readLong();
        return l;
    }
}
