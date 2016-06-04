package com.example.hammerox.oxsudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SudokuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        SudokuFragment sudokuFragment = new SudokuFragment();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_sudoku_container, sudokuFragment)
                    .commit();
        }
    }
}
