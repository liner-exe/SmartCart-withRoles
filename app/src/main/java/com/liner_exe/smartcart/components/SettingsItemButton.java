package com.liner_exe.smartcart.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liner_exe.smartcart.R;
import com.liner_exe.smartcart.databinding.ComponentSettingsItemButtonBinding;

public class SettingsItemButton extends LinearLayout {
    private ComponentSettingsItemButtonBinding binding;
    private TextView title;
    private TextView subtitle;
    private ImageView icon;

    public SettingsItemButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding = ComponentSettingsItemButtonBinding.inflate(
                LayoutInflater.from(context), this, true
        );

        title = binding.settingsItemTitle;
        icon = binding.settingsItemIcon;
        subtitle = binding.settingsItemSubtitle;


        this.setClickable(true);
        this.setFocusable(true);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsItemButton);

            String text = a.getString(R.styleable.SettingsItemButton_itemText);
            title.setText(text);

            String subtitleText = a.getString(R.styleable.SettingsItemButton_itemSubtitle);
            subtitle.setText(subtitleText);

            int img = a.getResourceId(R.styleable.SettingsItemButton_itemIcon, R.drawable.info_filled);
            icon.setImageResource(img);

            int defaultColor = Color.parseColor("#b3e5fc");
            int color = a.getColor(R.styleable.SettingsItemButton_itemColor, defaultColor);

            setRippleEffect(color);

            a.recycle();
        }
    }

    private void setRippleEffect(int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);

        RippleDrawable rippleDrawable = new RippleDrawable(
                ColorStateList.valueOf(Color.parseColor("#33000000")),
                shape,
                null
        );

        binding.drawerButtonContainer.setBackground(rippleDrawable);
    }

    public void setSubtitle(String text) {
        subtitle.setText(text);
    }
}
