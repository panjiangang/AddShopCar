package app.com.addshopcar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author ddy
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ban)
    Banner ban;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.bargainPrice)
    TextView bargainPrice;
    private Bean.DataBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ban.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage((String) path, imageView);
            }
        });
        price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        gethttp("https://www.zhaoapi.cn/product/getProductDetail?pid=71", 0);
    }

    private void gethttp(String s, final int i) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(s)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                Gson gson = new Gson();

                if (i == 0) {
                    data = gson.fromJson(response.body().string(), Bean.class).getData();

                    String[] strings = data.getImages().split("\\|");
                    final ArrayList<String> imas = new ArrayList<>();
                    for (int i = 0; i < strings.length; i++) {
                        imas.add(strings[i]);
                    }

                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            ban.setImages(imas);
                            ban.start();

                            title.setText(data.getTitle());
                            price.setText("原价:" + data.getPrice());
                            bargainPrice.setText("优惠价:" + data.getBargainPrice());
                        }
                    });
                } else if (i == 1) {

                    final String msg = gson.fromJson(response.body().string(), CartBean.class).getMsg();

                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        gethttp("https://www.zhaoapi.cn/product/addCart?uid=" + data.getSellerid() + "&pid=" + data.getPid(), 1);
    }
}
