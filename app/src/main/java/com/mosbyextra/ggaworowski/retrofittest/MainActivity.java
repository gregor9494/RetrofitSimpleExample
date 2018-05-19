package com.mosbyextra.ggaworowski.retrofittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        Disposable dis = RestClient.getInstance()
                .getService()
                .getUser(2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(dataModel -> {
                    UserModel userModel = dataModel.getData();
                    textView.setText(userModel.getId() + " : " + userModel.getFirstName());
                }, throwable -> {
                    textView.setText("Zjebało się");
                    throwable.printStackTrace();
                });

    }
}
