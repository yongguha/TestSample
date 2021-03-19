package com.ygha.myexpandable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        getData();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList("엘리베이터 점검 공지", "CCTV 확장 설치 공지", "지하주차장 물청소 공지");
        List<String> listContent = Arrays.asList(
                "안녕하십니까 관리사무에서 공지 사항을 알려드비니다.\n" +
                "엘리베이터 점검이 진행될 예정입니다. 일정은 다음과 같습니다.\n" +
                "2021년 3월 1일 오후 6시. 점검 기간 동안 엘리베이터 이용이 제한됩니다.",

                "안녕하십니까, 관리 사무소에서 공지 사항을 알려드립니다.\n" +
                "야간 활동을 위하여, 금번에 아파트 각 동마다 CCTV를 2대 증설 하기로 했습니다..\n" +
                "공사기간은 2021.1월30일 부터 2달간 진행됩니다. 공사로 인한 불편사항이 있더라도 \n"  +
                "많은 양해 부탁 드립니다.",

                "안녕하십니까 관리사무소에서 공지사항을 알려드립니다. 103동의\n" +
                "지하주차장의 수리가 끝나므로 인해, 103동 지하주차장의 물청소를 실시합니다.\n" +
                "2021년 3월 1일 오전 7시 부터 오후 7시 까지 물청소가 실시 되오니 103동 주민분들은 \n"+
                "옆동 지하주차장을 이용해주시기 바랍니다."
        );

        List<String> listDate = Arrays.asList("2021.01.19", "2021.03.02", "2021.03.18");

        List<Integer> listResId = Arrays.asList(
                R.drawable.ic_baseline_emoji_transportation_24,
                R.drawable.ic_baseline_face_24,
                R.drawable.ic_baseline_fireplace_24);

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setDate(listDate.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}