package com.unlam.soa.tp2.presenter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.unlam.soa.tp2.model.AndroidModel;
import com.unlam.soa.tp2.view.AndroidActivity;

public class AndroidPresenter extends BasePresenter{
    private final AndroidModel model;
    private final AndroidActivity activity;
    private final DisplayMetrics displayMetrics;

    public AndroidPresenter(AndroidActivity activity, ConstraintLayout constraintLayout) {
        super(activity, constraintLayout);
        this.activity = activity;
        this.model = new AndroidModel(this);

        displayMetrics = new DisplayMetrics();
        this.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    public int getHeight() {
        return this.displayMetrics.heightPixels;
    }

    public int getWidth() {
        return this.displayMetrics.widthPixels;
    }

    public void resetCirclePosition(ImageView circle, Integer width, Integer height) {
        circle.setX((float) (height / 2));
        circle.setY((float) (width / 2));
    }

    public void createBitMap(ImageView circle) {
        Bitmap bitMap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        bitMap = bitMap.copy(bitMap.getConfig(), true);
        Canvas canvas = new Canvas(bitMap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(25, 25, 15, paint);
        circle.setImageBitmap(bitMap);
    }

    @Override
    public void onDestroy() {
        this.model.onDestroy();
    }

}
