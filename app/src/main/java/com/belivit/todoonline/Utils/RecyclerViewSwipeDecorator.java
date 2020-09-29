package com.belivit.todoonline.Utils;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewSwipeDecorator {
    private Canvas canvas;
    private RecyclerView recyclerView;
    private RecyclerView.ViewHolder viewHolder;
    private float dX;
    private float dY;
    private int actionState;
    private boolean isCurrentlyActive;

    private int swipeLeftBackgroundColor;
    private int swipeLeftActionIconId;
    private Integer swipeLeftActionIconTint;

    private int swipeRightBackgroundColor;
    private int swipeRightActionIconId;
    private Integer swipeRightActionIconTint;

    private int iconHorizontalMargin;

    private String mSwipeLeftText;
    private float mSwipeLeftTextSize = 14;
    private int mSwipeLeftTextUnit = TypedValue.COMPLEX_UNIT_SP;
    private int mSwipeLeftTextColor = Color.DKGRAY;
    private Typeface mSwipeLeftTypeface = Typeface.SANS_SERIF;

    private String mSwipeRightText;
    private float mSwipeRightTextSize = 14;
    private int mSwipeRightTextUnit = TypedValue.COMPLEX_UNIT_SP;
    private int mSwipeRightTextColor = Color.DKGRAY;
    private Typeface mSwipeRightTypeface = Typeface.SANS_SERIF;

    private RecyclerViewSwipeDecorator() {
        swipeLeftBackgroundColor = 0;
        swipeRightBackgroundColor = 0;
        swipeLeftActionIconId = 0;
        swipeRightActionIconId = 0;
        swipeLeftActionIconTint = null;
        swipeRightActionIconTint = null;
    }

    public RecyclerViewSwipeDecorator(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        this();
        this.canvas = canvas;
        this.recyclerView = recyclerView;
        this.viewHolder = viewHolder;
        this.dX = dX;
        this.dY = dY;
        this.actionState = actionState;
        this.isCurrentlyActive = isCurrentlyActive;
        this.iconHorizontalMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, recyclerView.getContext().getResources().getDisplayMetrics());
    }

    public void setBackgroundColor(int backgroundColor) {
        this.swipeLeftBackgroundColor = backgroundColor;
        this.swipeRightBackgroundColor = backgroundColor;
    }

    public void setActionIconId(int actionIconId) {
        this.swipeLeftActionIconId = actionIconId;
        this.swipeRightActionIconId = actionIconId;
    }

    public void setActionIconTint(int color) {
        this.setSwipeLeftActionIconTint(color);
        this.setSwipeRightActionIconTint(color);
    }

    public void setSwipeLeftBackgroundColor(int swipeLeftBackgroundColor) {
        this.swipeLeftBackgroundColor = swipeLeftBackgroundColor;
    }

    public void setSwipeLeftActionIconId(int swipeLeftActionIconId) {
        this.swipeLeftActionIconId = swipeLeftActionIconId;
    }

    public void setSwipeLeftActionIconTint(int color) {
        swipeLeftActionIconTint = color;
    }

    public void setSwipeRightBackgroundColor(int swipeRightBackgroundColor) {
        this.swipeRightBackgroundColor = swipeRightBackgroundColor;
    }

    public void setSwipeRightActionIconId(int swipeRightActionIconId) {
        this.swipeRightActionIconId = swipeRightActionIconId;
    }

    public void setSwipeRightActionIconTint(int color) {
        swipeRightActionIconTint = color;
    }

    public void setSwipeRightLabel(String label) {
        mSwipeRightText = label;
    }

    public void setSwipeRightTextSize(int unit, float size) {
        mSwipeRightTextUnit = unit;
        mSwipeRightTextSize = size;
    }

    public void setSwipeRightTextColor(int color) {
        mSwipeRightTextColor = color;
    }

    public void setSwipeRightTypeface(Typeface typeface) {
        mSwipeRightTypeface = typeface;
    }

    @Deprecated
    public void setIconHorizontalMargin(int iconHorizontalMargin) {
        setIconHorizontalMargin(TypedValue.COMPLEX_UNIT_DIP, iconHorizontalMargin);
    }

    public void setIconHorizontalMargin(int unit, int iconHorizontalMargin) {
        this.iconHorizontalMargin = (int)TypedValue.applyDimension(unit, iconHorizontalMargin, recyclerView.getContext().getResources().getDisplayMetrics());
    }

    public void setSwipeLeftLabel(String label) {
        mSwipeLeftText = label;
    }

    public void setSwipeLeftTextSize(int unit, float size) {
        mSwipeLeftTextUnit = unit;
        mSwipeLeftTextSize = size;
    }

    public void setSwipeLeftTextColor(int color) {
        mSwipeLeftTextColor = color;
    }

    public void setSwipeLeftTypeface(Typeface typeface) {
        mSwipeLeftTypeface = typeface;
    }

    public void decorate() {
        try {
            if ( actionState != ItemTouchHelper.ACTION_STATE_SWIPE ) return;

            if ( dX > 0 ) {
                // Swiping Right
                canvas.clipRect(viewHolder.itemView.getLeft(), viewHolder.itemView.getTop(), viewHolder.itemView.getLeft() + (int) dX, viewHolder.itemView.getBottom());
                if ( swipeRightBackgroundColor != 0 ) {
                    final ColorDrawable background = new ColorDrawable(swipeRightBackgroundColor);
                    background.setBounds(viewHolder.itemView.getLeft(), viewHolder.itemView.getTop(), viewHolder.itemView.getLeft() + (int) dX, viewHolder.itemView.getBottom());
                    background.draw(canvas);
                }

                int iconSize = 0;
                if ( swipeRightActionIconId != 0 && dX > iconHorizontalMargin ) {
                    Drawable icon = ContextCompat.getDrawable(recyclerView.getContext(), swipeRightActionIconId);
                    if ( icon != null ) {
                        iconSize = icon.getIntrinsicHeight();
                        int halfIcon = iconSize / 2;
                        int top = viewHolder.itemView.getTop() + ((viewHolder.itemView.getBottom() - viewHolder.itemView.getTop()) / 2 - halfIcon);
                        icon.setBounds(viewHolder.itemView.getLeft() + iconHorizontalMargin, top, viewHolder.itemView.getLeft() + iconHorizontalMargin + icon.getIntrinsicWidth(), top + icon.getIntrinsicHeight());
                        if (swipeRightActionIconTint != null)
                            icon.setColorFilter(swipeRightActionIconTint, PorterDuff.Mode.SRC_IN);
                        icon.draw(canvas);
                    }
                }

                if ( mSwipeRightText != null && mSwipeRightText.length() > 0 && dX > iconHorizontalMargin + iconSize) {
                    TextPaint textPaint = new TextPaint();
                    textPaint.setAntiAlias(true);
                    textPaint.setTextSize(TypedValue.applyDimension(mSwipeRightTextUnit, mSwipeRightTextSize, recyclerView.getContext().getResources().getDisplayMetrics()));
                    textPaint.setColor(mSwipeRightTextColor);
                    textPaint.setTypeface(mSwipeRightTypeface);

                    int textTop = (int) (viewHolder.itemView.getTop() + ((viewHolder.itemView.getBottom() - viewHolder.itemView.getTop()) / 2.0) + textPaint.getTextSize()/2);
                    canvas.drawText(mSwipeRightText, viewHolder.itemView.getLeft() + iconHorizontalMargin + iconSize + (iconSize > 0 ? iconHorizontalMargin/2 : 0), textTop,textPaint);
                }

            } else if ( dX < 0 ) {
                // Swiping Left
                canvas.clipRect(viewHolder.itemView.getRight() + (int) dX, viewHolder.itemView.getTop(), viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());
                if ( swipeLeftBackgroundColor != 0 ) {
                    final ColorDrawable background = new ColorDrawable(swipeLeftBackgroundColor);
                    background.setBounds(viewHolder.itemView.getRight() + (int) dX, viewHolder.itemView.getTop(), viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());
                    background.draw(canvas);
                }

                int iconSize = 0;
                int imgLeft = viewHolder.itemView.getRight();
                if ( swipeLeftActionIconId != 0 && dX < - iconHorizontalMargin ) {
                    Drawable icon = ContextCompat.getDrawable(recyclerView.getContext(), swipeLeftActionIconId);
                    if ( icon != null ) {
                        iconSize = icon.getIntrinsicHeight();
                        int halfIcon = iconSize / 2;
                        int top = viewHolder.itemView.getTop() + ((viewHolder.itemView.getBottom() - viewHolder.itemView.getTop()) / 2 - halfIcon);
                        imgLeft = viewHolder.itemView.getRight() - iconHorizontalMargin - halfIcon * 2;
                        icon.setBounds(imgLeft, top, viewHolder.itemView.getRight() - iconHorizontalMargin, top + icon.getIntrinsicHeight());
                        if (swipeLeftActionIconTint != null)
                            icon.setColorFilter(swipeLeftActionIconTint, PorterDuff.Mode.SRC_IN);
                        icon.draw(canvas);
                    }
                }

                if ( mSwipeLeftText != null && mSwipeLeftText.length() > 0 && dX < - iconHorizontalMargin - iconSize ) {
                    TextPaint textPaint = new TextPaint();
                    textPaint.setAntiAlias(true);
                    textPaint.setTextSize(TypedValue.applyDimension(mSwipeLeftTextUnit, mSwipeLeftTextSize, recyclerView.getContext().getResources().getDisplayMetrics()));
                    textPaint.setColor(mSwipeLeftTextColor);
                    textPaint.setTypeface(mSwipeLeftTypeface);

                    float width = textPaint.measureText(mSwipeLeftText);
                    int textTop = (int) (viewHolder.itemView.getTop() + ((viewHolder.itemView.getBottom() - viewHolder.itemView.getTop()) / 2.0) + textPaint.getTextSize() / 2);
                    canvas.drawText(mSwipeLeftText, imgLeft - width - ( imgLeft == viewHolder.itemView.getRight() ? iconHorizontalMargin : iconHorizontalMargin/2 ), textTop, textPaint);
                }
            }
        } catch(Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }

    public static class Builder {
        private RecyclerViewSwipeDecorator mDecorator;

        public Builder(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            mDecorator = new RecyclerViewSwipeDecorator(
                    canvas,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
            );
        }

        public Builder addBackgroundColor(int color) {
            mDecorator.setBackgroundColor(color);
            return this;
        }

        public Builder addActionIcon(int drawableId) {
            mDecorator.setActionIconId(drawableId);
            return this;
        }

        public Builder setActionIconTint(int color) {
            mDecorator.setActionIconTint(color);
            return this;
        }

        public Builder addSwipeRightBackgroundColor(int color) {
            mDecorator.setSwipeRightBackgroundColor(color);
            return this;
        }

        public Builder addSwipeRightActionIcon(int drawableId) {
            mDecorator.setSwipeRightActionIconId(drawableId);
            return this;
        }

        public Builder setSwipeRightActionIconTint(int color) {
            mDecorator.setSwipeRightActionIconTint(color);
            return this;
        }

        public Builder addSwipeRightLabel(String label) {
            mDecorator.setSwipeRightLabel(label);
            return this;
        }

        public Builder setSwipeRightLabelColor(int color) {
            mDecorator.setSwipeRightTextColor(color);
            return this;
        }

        public Builder setSwipeRightLabelTextSize(int unit, float size) {
            mDecorator.setSwipeRightTextSize(unit, size);
            return this;
        }

        public Builder setSwipeRightLabelTypeface(Typeface typeface) {
            mDecorator.setSwipeRightTypeface(typeface);
            return this;
        }

        public Builder addSwipeLeftBackgroundColor(int color) {
            mDecorator.setSwipeLeftBackgroundColor(color);
            return this;
        }

        public Builder addSwipeLeftActionIcon(int drawableId) {
            mDecorator.setSwipeLeftActionIconId(drawableId);
            return this;
        }

        public Builder setSwipeLeftActionIconTint(int color) {
            mDecorator.setSwipeLeftActionIconTint(color);
            return this;
        }

        public Builder addSwipeLeftLabel(String label) {
            mDecorator.setSwipeLeftLabel(label);
            return this;
        }

        public Builder setSwipeLeftLabelColor(int color) {
            mDecorator.setSwipeLeftTextColor(color);
            return this;
        }

        public Builder setSwipeLeftLabelTextSize(int unit, float size) {
            mDecorator.setSwipeLeftTextSize(unit, size);
            return this;
        }

        public Builder setSwipeLeftLabelTypeface(Typeface typeface) {
            mDecorator.setSwipeLeftTypeface(typeface);
            return this;
        }

        @Deprecated
        public Builder setIconHorizontalMargin(int pixels) {
            mDecorator.setIconHorizontalMargin(pixels);
            return this;
        }

        public Builder setIconHorizontalMargin(int unit, int iconHorizontalMargin) {
            mDecorator.setIconHorizontalMargin(unit, iconHorizontalMargin);
            return this;
        }

        public RecyclerViewSwipeDecorator create() {
            return mDecorator;
        }
    }
}