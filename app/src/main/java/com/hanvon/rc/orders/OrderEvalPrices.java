package com.hanvon.rc.orders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanvon.rc.R;
import com.hanvon.rc.utils.LogUtil;

/**
 * @Desc:
 * @Auth: chenxzhuang
 * @Time: 2016/4/23 0023.
 */
public class OrderEvalPrices extends Activity implements View.OnClickListener {
    private ImageView IvBack;
    private TextView TvPricesInfo;
    private TextView TvFilename;
    private TextView TvPages;
    private TextView TvBytes;
    private TextView TvWaitTime;
    private TextView TvSysInfo;
    private EditText ETinfo;
    private TextView TvPrices;
    private TextView TvOrderPrices;
    private ImageView TvPay;

    private TextView TvContactsName;
    private TextView TvContactsPhone;
    private TextView TvContactsModify;

    private OrderDetail orderDetail;

    public static OrderEvalPrices instance = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.evalprice);

        Intent intent = getIntent();
        if (intent != null) {
            orderDetail = (OrderDetail)intent.getSerializableExtra("ordetail");
        }
        InitView();
    }

    public void InitView(){
        IvBack = (ImageView)findViewById(R.id.evalprice_back);
        TvPricesInfo = (TextView)findViewById(R.id.evalprice_priceinfo);
        TvFilename = (TextView)findViewById(R.id.evalprice_name);
        TvPages = (TextView)findViewById(R.id.evalprice_page);
        TvBytes = (TextView)findViewById(R.id.evalprice_bytes);
        TvWaitTime = (TextView)findViewById(R.id.evalprice_waittime);
        TvPrices = (TextView)findViewById(R.id.evalprice_prices);
        TvSysInfo = (TextView)findViewById(R.id.evalprice_sysinfo);
        ETinfo = (EditText)findViewById(R.id.evalprice_editinfo);
        TvOrderPrices = (TextView)findViewById(R.id.evalprice_evalprice);
        TvPay = (ImageView)findViewById(R.id.evalprice_topay);
        TvContactsModify = (TextView)findViewById(R.id.modify_name);
        TvContactsName = (TextView)findViewById(R.id.eval_name);
        TvContactsPhone = (TextView)findViewById(R.id.eval_phone);

        IvBack.setOnClickListener(this);
        TvPay.setOnClickListener(this);
        TvPricesInfo.setOnClickListener(this);
        TvContactsModify.setOnClickListener(this);

        SharedPreferences mSharedPreferences=getSharedPreferences("BitMapUrl", Activity.MODE_MULTI_PROCESS);
        String name = mSharedPreferences.getString("contactsname", "");
        String phone = mSharedPreferences.getString("contactsphone","");
        orderDetail.setOrderPhone(phone);
        orderDetail.setOrderName(name);

        TvContactsName.setText(orderDetail.getOrderName());
        TvContactsPhone.setText(orderDetail.getOrderPhone());
        TvFilename.setText(orderDetail.getOrderFileNanme());
        TvPages.setText(orderDetail.getOrderFilesPages()+" 张");
        TvBytes.setText(orderDetail.getOrderFilesBytes()+" 字节");
        TvWaitTime.setText(orderDetail.getOrderWaitTime());
        TvPrices.setText(orderDetail.getOrderPrice()+" 元");
        TvSysInfo.setText("现在下单，识别结果可在 "+orderDetail.getOrderFinshTime()+" 后查收，我们将以短信和邮件的形式通知您。");
        TvOrderPrices.setText(orderDetail.getOrderPrice()+" 元");
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.evalprice_back:
                finish();
                break;
            case R.id.evalprice_priceinfo:
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderEvalPrices.this);
                builder.setMessage("收费标准每千字30元");
                builder.setTitle("收费标准");
                builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.evalprice_topay:
                Intent intent = new Intent();
                intent.setClass(this,OrderToPay.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ordetail", orderDetail);
                intent.putExtras(bundle);
                intent.putExtra("from","EVAL_PRICES");
                startActivity(intent);
                finish();
                break;
            case R.id.modify_name:
                Intent intent1 = new Intent();
                intent1.setClass(this,ModifyContacts.class);
                intent1.putExtra("name", orderDetail.getOrderName());
                intent1.putExtra("phone",orderDetail.getOrderPhone());
                startActivityForResult(intent1, 1);
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i("------------------");
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case 2:
                String name = data.getStringExtra("name");
                String phone = data.getStringExtra("phone");
                orderDetail.setOrderName(name);
                orderDetail.setOrderPhone(phone);
                TvContactsName.setText(orderDetail.getOrderName());
                TvContactsPhone.setText(orderDetail.getOrderPhone());
                break;
            default:
                break;
        }
    }
}
