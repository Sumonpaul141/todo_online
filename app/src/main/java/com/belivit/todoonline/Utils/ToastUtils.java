package com.belivit.todoonline.Utils;

import android.content.Context;
import android.graphics.Color;

import com.belivit.todoonline.R;
import com.muddzdev.styleabletoast.StyleableToast;

public class ToastUtils {
    public static void showToastOk(Context context, String text) {
        new StyleableToast
                .Builder(context)
                .text(text)
                .textColor(Color.parseColor("#2162AF"))
                .stroke(1,Color.parseColor("#2162AF"))
                .backgroundColor(Color.parseColor("#ffffff"))
                .cornerRadius(3)
                .length(0)
                .show();
    }
    public static void showToastError(Context context, String text) {
        new StyleableToast
                .Builder(context)
                .text(text)
                .textColor(Color.parseColor("#F44336"))
                .backgroundColor(Color.parseColor("#ffffff"))
                .stroke(1,Color.parseColor("#F44336") )
                .cornerRadius(3)
                .length(0)
                .show();
    }

}
