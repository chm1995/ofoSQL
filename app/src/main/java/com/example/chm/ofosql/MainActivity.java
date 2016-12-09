package com.example.chm.ofosql;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.chm.ofosql.adapter.MyAdapter;
import com.example.chm.ofosql.baen.Data;
import com.example.chm.ofosql.db.ofodb;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mlistView;
    private EditText ed_id;
    private Button bt_add, bt_query;
    private String ofoid;
    private MyAdapter<ofodb> myAdapter = null;
    private List<ofodb> mOfodb = null;
    ofodb myofo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.initialize(this);
        initview();
        QuireAll();
    }

    public void Adddb(String id, String pwd) {
        ofodb db = new ofodb();
        db.setOfoid(id);
        db.setPwd(pwd);
        db.save();
        Log.d("TAG", "news id is " + db.getOfoid());
        Log.d("TAG", "news id is " + db.getPwd());
    }

    public void QuireAll() {
        List<ofodb> alldb = DataSupport.findAll(ofodb.class);
        setlistview(myofo.getOfoid().toString(),myofo.getPwd().toString());
    }

    public ofodb Quire(String id) {
        ofodb db = DataSupport.find(ofodb.class, Long.parseLong(id));
        return db;
    }

    public void setlistview(String id, String pwd) {
        mOfodb = new ArrayList<ofodb>();
        for (int i = 0;i< mOfodb.size(); i++) {
            mOfodb.add(new ofodb(id,pwd));
        }



        myAdapter = new MyAdapter<ofodb>((ArrayList) mOfodb, R.layout.list_item) {
            @Override
            public void bindView(ViewHolder holder, ofodb obj) {
                holder.setText(R.id.ofoid, obj.getOfoid());
                holder.setText(R.id.pwd, obj.getPwd());
            }
        };
        mlistView.setAdapter(myAdapter);
    }

    private void initData() {
        //获得搜索文本框的内容
        ofoid = ed_id.getText().toString();
        listen();
    }

    private void initview() {
        mlistView = (ListView) findViewById(R.id.myrecyclerview);
        ed_id = (EditText) findViewById(R.id.ed_id);
        bt_add = (Button) findViewById(R.id.add);
        bt_query = (Button) findViewById(R.id.query);
        myofo=new ofodb();
        initData();
    }

    public void listen() {
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog();
            }
        });
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Quire(ed_id.getText().toString());
            }
        });
    }

    public void Dialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.dialog));
        final EditText ofoid = (EditText) dialog.findViewById(R.id.et_ofoid);
        final EditText ofopwd = (EditText) dialog.findViewById(R.id.et_ofopwd);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加一辆车的信息!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Adddb(ofoid.getText().toString(), ofopwd.getText().toString());
            }
        });
        builder.setView(dialog);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.show();
    }
}
