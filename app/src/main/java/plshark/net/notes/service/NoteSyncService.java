package plshark.net.notes.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class NoteSyncService extends IntentService {

    public static final String SAVE_ACTION = "save";
    public static final String DELETE_ACTION = "delete";
    public static final String UPDATE_ACTION = "update";

    public NoteSyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case SAVE_ACTION:
                    break;
            }
        }
    }
}
