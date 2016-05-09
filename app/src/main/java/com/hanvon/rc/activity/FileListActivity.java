package com.hanvon.rc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanvon.rc.R;
import com.hanvon.rc.adapter.ResultFileListAdapter;
import com.hanvon.rc.db.FileInfo;
import com.hanvon.rc.md.camera.activity.RecResultActivity;
import com.hanvon.rc.utils.LogUtil;

import java.util.List;


public class FileListActivity extends Activity implements View.OnClickListener
{

    private RelativeLayout rlBottom;
    private RelativeLayout rlTitle;
    private ImageView ivBack;
    private TextView tvTitle;
    private ListView lvFile;

    private ImageView mIvShare;
    private ImageView mIvCopyFiles;
    private ImageView mIvDelFiles;

    private ResultFileListAdapter fileListAdapter;
    private List<FileInfo> mFileList;

    private static int EDIT_MODE = 2;
    private static int VIEW_MODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ret_list);

        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        rlBottom = (RelativeLayout) findViewById(R.id.ll_bottom_bar);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        lvFile = (ListView) findViewById(R.id.lv_file);

        mIvShare = (ImageView) findViewById(R.id.iv_share);
        mIvShare.setOnClickListener(this);
        mIvCopyFiles = (ImageView) findViewById(R.id.iv_copy_files);
        mIvCopyFiles.setOnClickListener(this);
        mIvDelFiles = (ImageView) findViewById(R.id.iv_del);
        mIvDelFiles.setOnClickListener(this);

        mFileList = MainActivity.dbManager.queryForAll();

        fileListAdapter = new ResultFileListAdapter(this, mFileList, VIEW_MODE);

        lvFile.setAdapter(fileListAdapter);
        lvFile.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
            {
                LogUtil.i("item " +  position + " clicked");
                startRecsultActivity(position);
                FileListActivity.this.finish();
            }
        });

    }

    private void startRecsultActivity(int pos)
    {
        FileInfo finfo = mFileList.get(pos);
        Intent intent = new Intent(this, RecResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", finfo);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back:
                this.finish();
            break;

            case R.id.iv_share:

                break;

            case R.id.iv_del:
                break;

            case R.id.iv_copy_files:
                break;

        }
    }

}

