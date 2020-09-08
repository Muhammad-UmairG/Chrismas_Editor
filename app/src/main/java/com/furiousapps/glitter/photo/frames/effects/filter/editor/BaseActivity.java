package com.furiousapps.glitter.photo.frames.effects.filter.editor;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class BaseActivity extends AppCompatActivity {
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    private AlertDialog mAlertDialog;

    protected void onStop() {
        super.onStop();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(BaseActivity.this, new String[]{permission}, requestCode);
                }
            }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    protected void showAlertDialog(@Nullable String title, @Nullable String message, @Nullable OnClickListener onPositiveButtonClickListener, @NonNull String positiveText, @Nullable OnClickListener onNegativeButtonClickListener, @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }
}
