package com.axb.upgrade.dialog;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.axb.upgrade.R;

public class NewVersionDialog extends BaseDialog{
    TextView mTitleTV;
    TextView mContentTV;
    TextView mOkBtn;
    TextView mCancelBtn;
    private OnConfirmListener listener;

    public NewVersionDialog(@NonNull Context context) {
        super(context);
    }

    protected int getLayoutResource() {
        return R.layout.dialog_new_version;
    }

    protected void bindWidgets() {
        this.mTitleTV = (TextView)this.findViewById(R.id.tvTitle);
        this.mContentTV = (TextView)this.findViewById(R.id.id_msg_content_tv);
        this.mOkBtn = (TextView)this.findViewById(R.id.id_ok_btn);
        this.mOkBtn.setOnClickListener((v) -> {
            this.onConfirm(true);
            this.dismiss();
        });
        this.mCancelBtn = (TextView)this.findViewById(R.id.id_cancel_btn);
        this.mCancelBtn.setOnClickListener((v) -> {
            this.onConfirm(false);
            this.dismiss();
        });
    }

    protected int getWidth() {
        return this.mContext.getResources().getDimensionPixelSize(R.dimen.new_version_dialog_width);
    }

    protected int getHeight() {
        return this.mContext.getResources().getDimensionPixelSize(R.dimen.new_version_dialog_height);
    }

    private void onConfirm(boolean accepted) {
        if (this.listener != null) {
            this.listener.onConfirm(this, accepted);
        }

    }

    public void setListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    public void setMsgContent(String msgContent) {
        this.mContentTV.setText(msgContent);
    }

    public void setMsgContent(int msgContent) {
        this.mContentTV.setText(msgContent);
    }

    public void setMsgTitle(String msgTitle) {
        this.mTitleTV.setText(msgTitle);
    }

    public interface OnConfirmListener {
        void onConfirm(BaseDialog var1, boolean var2);
    }
}
