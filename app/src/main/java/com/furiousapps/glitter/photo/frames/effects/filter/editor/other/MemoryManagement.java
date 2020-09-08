package com.furiousapps.glitter.photo.frames.effects.filter.editor.other;

import android.app.ActivityManager;
import android.content.Context;

public class MemoryManagement {
    public static float free(Context context) {
        int memoryClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() - 24;
        if (memoryClass < 1) {
            memoryClass = 1;
        }
        return (float) memoryClass;
    }
}
