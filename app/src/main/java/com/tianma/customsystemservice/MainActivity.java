package com.tianma.customsystemservice;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tianma.customsystemservice.aidl.CustomService;
import com.tianma.customsystemservice.aidl.ICustomService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn_test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ICustomService customService = CustomService.getService();
                if (customService == null) {
                    Toast.makeText(v.getContext(), "CustomService register failed", Toast.LENGTH_LONG).show();
                    return;
                }
                StringBuilder sb = new StringBuilder();
                try {
                    int result = customService.add(1, 2);
                    sb.append("1 + 2 = ").append(result).append("\n");
                    String str = "hello";
                    sb.append(str).append(" -> ").append(customService.toUpperCase(str));
                } catch (RemoteException e) {
                    e.printStackTrace();
                    sb.append(e.getMessage());
                }

                Toast.makeText(v.getContext(), sb.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
