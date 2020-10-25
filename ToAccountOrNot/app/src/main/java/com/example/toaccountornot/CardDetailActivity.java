package com.example.toaccountornot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toaccountornot.utils.Day;
import com.example.toaccountornot.utils.DayAdapter;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class CardDetailActivity extends AppCompatActivity {

    private String year;
    private String month;
    private TextView label;
    private TextView label_year;
    private TextView label_month;
    private TextView label_out;
    private TextView label_in;
    private LinearLayout choose_date;
    private RecyclerView rec_day;
    private List<Day> dayList = new ArrayList<>();
    private String cardname;

    @Override
    protected void onResume(){
        super.onResume();
        initDaylist();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        Calendar calendar = Calendar.getInstance();
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        initView();
        //Bundle bundle = this.getIntent().getExtras();
        //cardname = bundle.getString("name");
        //Toast.makeText(CardDetailActivity.this, cardname, Toast.LENGTH_LONG).show();
    }
    void initView() {
        Bundle bundle = this.getIntent().getExtras();
        cardname = bundle.getString("name");
        label = findViewById(R.id.label);
        label_year = findViewById(R.id.label_year);
        label_month = findViewById(R.id.label_month);
        label_out = findViewById(R.id.label_out);
        label_in = findViewById(R.id.label_in);
        choose_date = findViewById(R.id.choose_date);
        rec_day = findViewById(R.id.mainlist);
        label.setText(cardname);
        label_year.setText(year);
        label_month.setText(month);
        rec_day.setLayoutManager(new LinearLayoutManager(getContext()));

        choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 待补充 使用DatePicker
            }
        });
    }
    void initDaylist() {
        dayList.clear();
        // 因为LitePal不支持group by, 故使用SQL语句查询
        Cursor cursor = LitePal.findBySQL("select date from Accounts where card = ?"+"and date_year=?" +
                        "and date_month=? group by date order by date desc",
                label.getText().toString(),
                label_year.getText().toString(),
                label_month.getText().toString());
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                dayList.add(new Day(date));
                DayAdapter dayAdapter = new DayAdapter(dayList, getContext());
                rec_day.setAdapter(dayAdapter);
            } while (cursor.moveToNext());

            double outcome = 0;
            double income = 0;
            for (Day day : dayList) {
                outcome += day.getOutcome_day();
                income += day.getIncome_day();
                DecimalFormat df = new DecimalFormat("#.##");

                label_out.setText(String.valueOf(df.format(outcome)));
                label_in.setText(String.valueOf(df.format(income)));
            }
        } else {
            DayAdapter dayAdapter = new DayAdapter(dayList, getContext());
            rec_day.setAdapter(dayAdapter);
            label_out.setText("0");
            label_in.setText("0");
        }
    }
}