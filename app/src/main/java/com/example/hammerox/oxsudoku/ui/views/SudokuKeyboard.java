package com.example.hammerox.oxsudoku.ui.views;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.utils.GameTools;


public class SudokuKeyboard {

    private static int activeKey = 0;
    private static Boolean pencilMode = false;
    private static Boolean eraseMode = false;

    private Activity activity;
    private View rootView;
    private SudokuGrid sudokuGrid;
    private float defaultSize;

    public static ColorStateList mColorPrimaryLight;
    public static ColorStateList mColorAccent;
    public static ColorStateList mColorBackground;

    public SudokuKeyboard(Activity activity, View rootView, SudokuGrid sudokuGrid) {
        this.activity = activity;
        this.rootView = rootView;
        this.sudokuGrid = sudokuGrid;
        defaultSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, activity.getResources().getDisplayMetrics());

        mColorAccent = ContextCompat.getColorStateList(activity, R.color.accent);
        mColorPrimaryLight = ContextCompat.getColorStateList(activity, R.color.primary_light);
        mColorBackground = ContextCompat.getColorStateList(activity, R.color.background);
    }


    public void drawKeyboard() {
        // Getting each button from keyboard's view.
        for (int key = 1; key <= 9; key++) {
            String idString = "key_" + key;
            int id = activity.getResources()
                    .getIdentifier(idString, "id", activity.getPackageName());
            Button keyButton = (Button) rootView.findViewById(id);

            // Changing appearance and behavior
            keyButton.setOnClickListener(keyboardListener());
            setKeyAppearance(keyButton, key);
        }
    }


    public void setKeyAppearance(Button keyButton, int key) {
        ViewCompat.setBackgroundTintList(keyButton, mColorPrimaryLight);
        keyButton.setText(String.valueOf(key));
        keyButton.setGravity(Gravity.CENTER);
        keyButton.setTypeface(Typeface.DEFAULT_BOLD);
        keyButton.setTextSize(defaultSize);
    }


    public void setSideTools() {

        GameTools tools = new GameTools(rootView, sudokuGrid);

        Button leftButton1 = (Button) rootView.findViewById(R.id.left_button_1);
        ViewCompat.setBackgroundTintList(leftButton1, SudokuKeyboard.mColorBackground);
        leftButton1.setOnClickListener(tools.getPencil());

        Button leftButton2 = (Button) rootView.findViewById(R.id.left_button_2);
        ViewCompat.setBackgroundTintList(leftButton2, SudokuKeyboard.mColorBackground);
        leftButton2.setOnClickListener(tools.getEraser());

        Button rightButton1 = (Button) rootView.findViewById(R.id.right_button_1);
        ViewCompat.setBackgroundTintList(rightButton1, SudokuKeyboard.mColorBackground);
        rightButton1.setOnClickListener(tools.getCheckAnswer());

        Button rightButton2 = (Button) rootView.findViewById(R.id.right_button_2);
        ViewCompat.setBackgroundTintList(rightButton2, SudokuKeyboard.mColorBackground);
        rightButton2.setOnClickListener(tools.getUndo());
    }


    public View.OnClickListener keyboardListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickKey((Button) v);
            }
        };
    }

    private void clickKey(Button button) {
        int pressedKeyNumber = Integer.valueOf(button.getText().toString());
        if (pressedKeyNumber != activeKey) {
            activateKey(button, pressedKeyNumber);
        } else {
            clearActiveKey(button);
        }
    }

    private void activateKey(Button pressedKey, int pressedKeyNumber) {
        if (activeKey != 0) {
            resetAppearanceOfLastKey();
        }
        highlightKeyAppearance(pressedKey);
        highlightGrid(pressedKeyNumber);
        activeKey = pressedKeyNumber;
    }

    private void clearActiveKey(Button pressedKey) {
        setButtonColor(pressedKey, mColorPrimaryLight);
        if (isGameComplete()) {
            hideButton(activity, activeKey);
        }
        clearHighlight();
        activeKey = 0;
    }



    private void clearHighlight() {
        sudokuGrid.clearPencilHighlight(activity, activeKey);
        sudokuGrid.clearPuzzleHighlight(activity);
    }

    private void highlightGrid(int pressedKeyNumber) {
        sudokuGrid.showPencilHighligh(activity, activeKey, pressedKeyNumber);
        sudokuGrid.showHighlight(activity, pressedKeyNumber);
    }

    private void highlightKeyAppearance(Button pressedKey) {
        showButton(pressedKey);
        setButtonColor(pressedKey, mColorAccent);
    }

    private void resetAppearanceOfLastKey() {
        Button lastKey = getLastKey();
        if (isGameComplete()) {
            hideButton(lastKey);
        } else {
            showButton(lastKey);
        }
        // BUG - Below is the only call that doesn't work correctly on API 21.
        // See comment inside setButtonColor() for more info.
        setButtonColor(lastKey, mColorPrimaryLight);
    }

    private Boolean isGameComplete() {
        int listIndex = activeKey - 1;
        return sudokuGrid.getIsNumberComplete().get(listIndex);
    }

    private Button getLastKey() {
        String idString = "key_" + activeKey;
        int id = activity.getResources()
                .getIdentifier(idString, "id", activity.getPackageName());
        return (Button) rootView.findViewById(id);
    }


    public static void showButton(Button key) {
        Drawable mDrawable = key.getBackground();
        mDrawable.setAlpha(255);
    }


    public static void hideButton(Button key) {
        Drawable mDrawable = key.getBackground();
        mDrawable.setAlpha(0);
    }


    public static void showButton(Activity activity, int number) {
        String idString = "key_" + number;
        int id = activity.getResources()
                .getIdentifier(idString, "id", activity.getPackageName());
        Button key = (Button) activity.findViewById(id);
        Drawable mDrawable = key.getBackground();
        mDrawable.setAlpha(255);
    }


    public static void hideButton(Activity activity, int number) {
        String idString = "key_" + number;
        int id = activity.getResources()
                .getIdentifier(idString, "id", activity.getPackageName());
        Button key = (Button) activity.findViewById(id);
        Drawable mDrawable = key.getBackground();
        mDrawable.setAlpha(0);
    }

    public static void setButtonColor(Button button, ColorStateList tint) {
        /* For some reason, setBackgroundTintList() does not work correctly on API 21.
         * Some calls to it works OK, but on a few it does not apply tint until something is triggered.
         * I found out two triggers to get it to work:
         * It works when onPause() is called or when the button toggle between setEnabled().
         * The only call that does not work on this program is commented next to a setButtonColor().
         * http://stackoverflow.com/questions/27735890/lollipops-backgroundtint-has-no-effect-on-a-button
         * http://stackoverflow.com/questions/36114388/setbackgroundtintlist-does-not-apply-directly
         */
        ViewCompat.setBackgroundTintList(button, tint);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            button.setEnabled(false);
            button.setEnabled(true);
        }
    }


    public static int getActiveKey() {
        return activeKey;
    }

    public static void setActiveKey(int activeKey) {
        SudokuKeyboard.activeKey = activeKey;
    }

    public static Boolean getEraseMode() {
        return eraseMode;
    }

    public static void setEraseMode(Boolean eraseMode) {
        SudokuKeyboard.eraseMode = eraseMode;
    }

    public static Boolean getPencilMode() {
        return pencilMode;
    }

    public static void setPencilMode(Boolean pencilMode) {
        SudokuKeyboard.pencilMode = pencilMode;
    }
}
