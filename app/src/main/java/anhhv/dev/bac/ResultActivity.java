package anhhv.dev.bac;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ResultActivity extends AppCompatActivity {
    TextView tvSex, tvHeight, tvTypeDrank, tvVehicle, tvResult, tvDate, tvDvDate, tvHour, tvDvHour, tvMinute, tvDvMinute, tvMoney, tvDriverLicense, tvDvMoney;
    Button btnBack;
    int sex, typeDrank, vehicle;
    float height, drank, T, B, C, A, V, c, r, resultC, resultB, resultT;
    int Ts, date, hour, minute;
    // BAC: Blood Alcohol Concentration - Nồng độ cồn trong hơi thở
    // T: tốc độ đào thải nồng độ cồn trong máu (giờ), Ts (T đơn vị giây)
    // B: nồng độ cồn trong khí thở (mg / lít khí thở)
    // C: nồng độ cồn trong máu (mg / ml máu)
    // A: đơn vị cồn (Đơn vị cồn = Dung tích (ml) x Nồng độ (%) x 0,79 (hệ số quy đổi); 1 đơn vị cồn tương đương 10g cồn)
    // height: cân nặng, drank: số lượng uống, V: đơn vị thể tích uống, c: nồng độ cồn, r: hằng số hấp thụ rượu (nam là 0.7 và nữ là 0.6)

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        sex = intent.getIntExtra(HomeActivity.KEY_SEX, HomeActivity.MALE);
        typeDrank = intent.getIntExtra(HomeActivity.KEY_TYPE_DRANK, HomeActivity.BRANDY);
        vehicle = intent.getIntExtra(HomeActivity.KEY_VEHICLE, HomeActivity.CAR);
        height = intent.getFloatExtra(HomeActivity.KEY_HEIGHT, 0);
        drank = intent.getFloatExtra(HomeActivity.KEY_DRANK, 0);

        int intHeight = (int) height;
        String stringHeight = (height == (float) intHeight) ? Integer.toString(intHeight) : Float.toString(height);
        int intDrank = (int) drank;
        String stringDrank = (drank == (float) intDrank) ? Integer.toString(intDrank) : Float.toString(drank);

        switch (typeDrank) {
            case HomeActivity.BEER: {
                V = 330F;
                c = 0.05F;
                break;
            }
            case HomeActivity.BRANDY: {
                V = 40F;
                c = 0.3F;
                break;
            }
            case HomeActivity.WINE: {
                V = 100F;
                c = 0.135F;
                break;
            }
        }

        if (sex == HomeActivity.MALE) {
            r = 0.7F;
        } else {
            r = 0.6F;
        }

        A = drank * V * c * 0.79F;
        C = 1056F * A / (10F * height * r); //(đơn vị mg/100ml máu)
        B = C / 210F; //(đơn vị mg/ lít khí thở)
        T = C / 15F;

        Ts = (int) (T * 3600);
//        Toast.makeText(this,""+T,Toast.LENGTH_SHORT).show();
        date = (int) TimeUnit.SECONDS.toDays(Ts);
        hour = (int) (TimeUnit.SECONDS.toHours(Ts) - TimeUnit.DAYS.toHours(date));
        minute = (int) (TimeUnit.SECONDS.toMinutes(Ts) - TimeUnit.DAYS.toMinutes(date) - TimeUnit.HOURS.toMinutes(hour));

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat formatter1 = (DecimalFormat) nf;
        formatter1.applyPattern("#.#");
        DecimalFormat formatter2 = (DecimalFormat) nf;
        formatter2.applyPattern("#.##");

        String formattedC = formatter2.format(C);
        String formattedB = formatter2.format(B);
//        String formattedT = formatter1.format(T);

        resultC = Float.parseFloat(formattedC);
        resultB = Float.parseFloat(formattedB);
//        resultT = Float.parseFloat(formattedT);

        tvSex = findViewById(R.id.tvSex);
        tvHeight = findViewById(R.id.tvHeight);
        tvTypeDrank = findViewById(R.id.tvTypeDrank);
        tvVehicle = findViewById(R.id.tvVehicle);
        tvResult = findViewById(R.id.tvResult);
        tvDate = findViewById(R.id.tvDate);
        tvDvDate = findViewById(R.id.tvDvDate);
        tvHour = findViewById(R.id.tvHour);
        tvDvHour = findViewById(R.id.tvDvHour);
        tvMinute = findViewById(R.id.tvMinute);
        tvDvMinute = findViewById(R.id.tvDvMinute);
        tvMoney = findViewById(R.id.tvMoney);
        tvDriverLicense = findViewById(R.id.tvDriverLicense);
        tvDvMoney = findViewById(R.id.tvDvMoney);
        btnBack = findViewById(R.id.btnBack);

        tvSex.setText((sex == HomeActivity.MALE) ? "Nam" : "Nữ");
        tvHeight.setText(stringHeight.replace(".", ",") + " kg");
        tvVehicle.setText((vehicle == HomeActivity.MOTO) ? "Xe máy" : (vehicle == HomeActivity.CAR) ? "Ô tô" : "Xe thô sơ");
        tvTypeDrank.setText(stringDrank + (((typeDrank == HomeActivity.BEER) ? " lon bia" : (typeDrank == HomeActivity.BRANDY) ? " chén rượu" : " ly vang")));

        tvResult.setText("Nồng độ cồn: " + (resultC + "").replace(".", ",") + " mg/100 ml máu, tương đương " + (resultB + "").replace(".", ",") + " mg/lít khí thở");
//        tvTime.setText("" + (resultT + "").replace(".",","));

        if (date == 0) {
            tvDate.setText("");
            tvDvDate.setVisibility(View.GONE);
        } else {
            tvDate.setText("" + date);
            tvDvDate.setVisibility(View.VISIBLE);
        }

        if (hour == 0) {
            tvHour.setText("");
            tvHour.setVisibility(View.GONE);
        } else {
            tvHour.setText("" + hour);
            tvHour.setVisibility(View.VISIBLE);
        }

        if (minute == 0) {
            tvMinute.setText("");
            tvMinute.setVisibility(View.GONE);
        } else {
            tvMinute.setText("" + minute);
            tvMinute.setVisibility(View.VISIBLE);
        }

        punishment(vehicle, resultC);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void punishment(int vehicle, float resultC) {
        //Theo quy định tại Nghị định 100/2019/NĐ-CP
        switch (vehicle) {
            case HomeActivity.MOTO: {
                if (resultC > 0 && resultC <= 50) {
                    tvMoney.setText("2 - 3"); //điểm c khoản 6 điều 6
                    tvDriverLicense.setText("10 - 12"); //điểm đ khoản 10 điều 6
                } else if (resultC > 50 && resultC <= 80) {
                    tvMoney.setText("4 - 5"); //điểm c khoản 7 điều 6
                    tvDriverLicense.setText("16 - 18"); //điểm e khoản 10 điều 6
                } else {
                    tvMoney.setText("6 - 8"); //điểm e khoản 8 điều 6
                    tvDriverLicense.setText("22 - 24"); //điểm g khoản 10 điều 6
                }
                break;
            }
            case HomeActivity.CAR: {
                if (resultC > 0 && resultC <= 50) {
                    tvMoney.setText("6 - 8"); //điểm c khoản 6 điều 5
                    tvDriverLicense.setText("10 - 12"); //điểm e khoản 11 điều 5
                } else if (resultC > 50 && resultC <= 80) {
                    tvMoney.setText("16 - 18"); //điểm c khoản 8 điều 5
                    tvDriverLicense.setText("16 - 18"); //điểm g khoản 11 điều 5
                } else {
                    tvMoney.setText("30 - 40"); //điểm a khoản 10 điều 5
                    tvDriverLicense.setText("22 - 24"); //điểm h khoản 11 điều 5
                }
                break;
            }
            case HomeActivity.BIKE: {
                if (resultC > 0 && resultC <= 50) {
                    tvMoney.setText("80 - 100");
                    tvDvMoney.setText("nghìn đồng");
                    tvDriverLicense.setText("0");

                } else if (resultC > 50 && resultC <= 80) {
                    tvMoney.setText("300 - 400");
                    tvDvMoney.setText("nghìn đồng");
                    tvDriverLicense.setText("0");
                } else {
                    tvMoney.setText("400 - 600");
                    tvDvMoney.setText("nghìn đồng");
                    tvDriverLicense.setText("0");
                }
                break;
            }
        }
    }
}
