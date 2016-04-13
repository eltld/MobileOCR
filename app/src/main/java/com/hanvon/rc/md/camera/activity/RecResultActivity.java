package com.hanvon.rc.md.camera.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanvon.rc.R;

/**
 * Created by baiheng222 on 16-4-5.
 */


public class RecResultActivity extends Activity implements View.OnClickListener
{

    private static final String TAG = "RecResultActivity";
    private ImageView ivBack;
    private TextView tvSave;

    private TextView tvShare;
    private TextView tvCopy;
    private TextView tvExact;
    private TextView tvDel;
    private TextView tvTitel;

    private EditText etResult;

    private String recResult;



    private final int DLG_RETURN_YES = 0;
    private final int DLG_RETURN_NO = 1;
    private final int DLG_RETURN_OK = 2;
    private final int DLG_RETURN_CANCEL = 3;
    private final int DLG_RETURN_INIT = -1;

    private int dlgReturn = DLG_RETURN_INIT;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rec_result_activity);

        initData();

        initView();

        setListener();

    }

    private void initView()
    {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitel = (TextView) findViewById(R.id.tv_title);
        tvSave = (TextView) findViewById(R.id.tv_save);

        tvShare = (TextView) findViewById(R.id.tv_share);
        tvCopy = (TextView) findViewById(R.id.tv_copy);
        tvExact = (TextView) findViewById(R.id.tv_exact);
        tvDel = (TextView) findViewById(R.id.tv_del);

        etResult = (EditText) findViewById(R.id.et_result);
        if (recResult != null)
        {
            etResult.setText(recResult);
        }
    }

    private void setListener()
    {
        ivBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvCopy.setOnClickListener(this);
        tvExact.setOnClickListener(this);
        tvDel.setOnClickListener(this);
    }

    private void initData()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            Bundle bundle = intent.getExtras();
            recResult = bundle.getString("textResult");
            if (null != recResult)
            {
                Log.d(TAG, "recResult is " + recResult);
            }
        }
    }


    private void showSave()
    {
        showDlg("Save file?");

    }

    private void showDel()
    {
        showDlg("Delete file?");

    }

    private void showDlg(String title)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(title);
        dialog.setNegativeButton(R.string.dlg_str_no, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dlgReturn = DLG_RETURN_NO;
            }
        });

        dialog.setPositiveButton(R.string.dlg_str_yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dlgReturn = DLG_RETURN_YES;
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.iv_back:
                break;

            case R.id.tv_save:
                showSave();
                break;

            case R.id.tv_share:
                break;

            case R.id.tv_copy:
                break;

            case R.id.tv_del:
                showDel();
                break;

            case R.id.tv_exact:
                break;
        }
    }

}
