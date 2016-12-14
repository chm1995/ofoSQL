package com.example.chm.ofosql;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chm.ofosql.adapter.MyAdapter;
import com.example.chm.ofosql.db.ofodb;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ListView mlistView;
    private TextView bt_add, bt_query;
    private MyAdapter<ofodb> myAdapter = null;
    private List<ofodb> mOfodb = null;
    private ofodb db;
    String strquireid = null;
    String strquirepwd = null;
    String strquiid=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.initialize(this);
        initview();
    }

    /**
     * 添加一条数据
     *
     * @param id
     * @param pwd
     */
    public void Adddb(String id, String pwd) {
        db = new ofodb();
        db.setOfoid(id);
        db.setPwd(pwd);
        db.save();
        if (db.save()) {
            QuireAll();
            showmsgDialog("添加成功");
        } else {
            showmsgDialog("添加失败");
        }
    }

    /**
     * 修改数据
     *
     * @param column
     * @param olddata
     * @param newdata
     */
    public void updatedata(String column, String olddata, String newdata) {
        ContentValues values = new ContentValues();
        values.put(column, newdata);
        int  ret =DataSupport.updateAll(ofodb.class, values, column + " = ?", olddata);
        if (ret>0){
            QuireAll();
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        }else{
            showmsgDialog("修改失败");
        }
    }

    /**
     * 删除数据
     */
    public void deletedata(String id) {
       int  ret= DataSupport.deleteAll(ofodb.class, "ofoid = ? ", id);
        if (ret>0){
            QuireAll();
            showmsgDialog("删除成功");
        }else{
            showmsgDialog("删除失败");
        }
    }

    /**
     * 查询所有数据
     * 返回list
     */
    public void QuireAll() {
        mOfodb = DataSupport.findAll(ofodb.class);
        setlistview();
    }

    /**
     * 根据id进行条件查询
     *
     * @param id
     */
    public void Quire(String id) {
        //String aa= DataSupport.find(ofodb.class,1).getOfoid().toString();
        if (!TextUtils.equals(id, " ")) {
            mOfodb = DataSupport.where("ofoid = ?", String.valueOf(id)).find(ofodb.class);
            if (mOfodb.size() > 0) {
                for (int i = 0; i < mOfodb.size(); i++) {
                    db = mOfodb.get(i);
                }
                strquireid = db.getOfoid().toString();
                strquirepwd = db.getPwd().toString();
            } else {
                db = null;
                Toast.makeText(this, "数据库无此车信息", Toast.LENGTH_SHORT).show();
                //addDialog();
            }
            //db = DataSupport.where("ofoid = ?", id).find(ofodb.class).get(0);
        }
    }

    /**
     * 填充listview
     */
    public void setlistview() {
        myAdapter = new MyAdapter<ofodb>(mOfodb, R.layout.list_item) {
            @Override
            public void bindView(ViewHolder holder, ofodb obj) {
                holder.setText(R.id.ofoid, obj.getOfoid());
                holder.setText(R.id.pwd, obj.getPwd());
            }
        };
        mlistView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化视图
     */
    private void initview() {
        mlistView = (ListView) findViewById(R.id.myrecyclerview);
        bt_add = (TextView) findViewById(R.id.add);
        bt_query = (TextView) findViewById(R.id.query);
        db = new ofodb();
        listen();
        QuireAll();
    }

    /**
     * 监听事件
     */
    public void listen() {
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog();
            }
        });
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuireTextDialog();
            }
        });
    }
    private void showmsgDialog(String msg) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("提示!");
        builder.setMessage(msg);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.show();
    }
    /**
     * 搜索结果的对话框
     *
     * @param pwd
     */
    private void QuireDialog(final String pwd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("车牌号:" +strquiid + "密码如下:");
        builder.setMessage(pwd);
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDialog(strquiid, pwd);
            }
        });
        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletedata(strquiid);
            }
        });
        builder.show();
    }


    /**
     * 修改数据的对话框
     */
    public void updateDialog(final String oldid, final String oldpwd) {
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.dialog));
        final EditText ofoid = (EditText) dialog.findViewById(R.id.et_ofoid);
        final EditText ofopwd = (EditText) dialog.findViewById(R.id.et_ofopwd);
        ofoid.setText(oldid);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改一辆车的信息!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updatedata("ofoid", oldid, ofoid.getText().toString());
                updatedata("pwd", oldpwd, ofopwd.getText().toString());

            }
        });
        builder.setView(dialog);
        builder.setIcon(R.drawable.ico);
        builder.show();
    }
    private void QuireTextDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("查询车辆密码!");
        builder.setMessage("请输入车牌号:");
        final EditText ed_Quireid=new EditText(this);
        ed_Quireid.setKeyListener(new DigitsKeyListener(false,true));
        ed_Quireid.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        if (!TextUtils.equals(strquiid,"")){
            ed_Quireid.setText(strquiid);
        }
        builder.setView(ed_Quireid);
        builder.setNegativeButton("清空", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                strquiid=null;
                QuireTextDialog();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                strquiid=ed_Quireid.getText().toString();
                if(StringUtils.checkID(strquiid)) {
                    Quire(strquiid);
                    if (db != null) {
                        if (db.getPwd().toString() != null && !db.getPwd().toString().equals(" ")) {
                            QuireDialog(db.getPwd().toString());
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"你输入的车牌不对",Toast.LENGTH_SHORT).show();
                    QuireTextDialog();
                }
            }
        });
        builder.show();
    }
    /**
     * 添加数据的对话框
     */
    public void addDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.dialog));
        final EditText ofoid = (EditText) dialog.findViewById(R.id.et_ofoid);
        final EditText ofopwd = (EditText) dialog.findViewById(R.id.et_ofopwd);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加一辆车的信息!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ifstring(ofoid.getText().toString(), ofopwd.getText().toString());
            }
        });
        builder.setView(dialog);
        builder.setIcon(R.drawable.ico);
        builder.show();
    }



    public void ifstring(String strofoid, String strpwd) {
        if (StringUtils.checkID(strofoid)) {
            if (StringUtils.checkPassword(strpwd)) {
                Quire(strofoid);
                if (!strofoid.equals(strquireid)) {
                    Adddb(strofoid, strpwd);
                } else {
                    Toast.makeText(getApplicationContext(), "车辆信息已存在!", Toast.LENGTH_SHORT).show();
                    strquiid=strofoid;
                    QuireDialog(strquireid);
                }
            } else {
                Toast.makeText(getApplicationContext(), "密码格式不对!", Toast.LENGTH_SHORT).show();
                addDialog();
            }
        } else {
            Toast.makeText(getApplicationContext(), "车牌格式不对", Toast.LENGTH_SHORT).show();
            addDialog();
        }
    }
}
