package com.codershubham.quickcarhire.Adapter;

import android.os.Bundle;

public class DataAdapter {
    private static DataAdapter instance;
    private Bundle data;

    private DataAdapter() {
        data = new Bundle();
    }

    public static DataAdapter getInstance() {
        if (instance == null) {
            instance = new DataAdapter();
        }
        return instance;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }
}
