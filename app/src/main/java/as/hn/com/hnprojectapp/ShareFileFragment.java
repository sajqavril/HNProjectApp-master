package as.hn.com.hnprojectapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.business.Data.ServiceHelper;
import com.hn.business.Data.UserInfoEntity;

import java.util.ArrayList;
import java.util.List;


public class ShareFileFragment extends Fragment {

    public static final String FRAGMENT_TITLE = "fragmentTitle";
    public static final String UPLOAD = "Upload";//已接收
    public static final String RECEIVE = "Receive";//已上传
    public static final String LOADING = "Loading";//正在传输
    public static final String TAG = "ShareFileFragment";

    private String fragmentTitle;
    private static String searchWord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentTitle = getArguments().getString(FRAGMENT_TITLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.chat_list_fragment,container,false);

        //设置当前的recyclerView的adapter
        setUpRecyclerView(recyclerView);

        return recyclerView;
    }

    public static Fragment newInstance(String fragmentTitle){
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TITLE,fragmentTitle);
        ShareFileFragment fragment = new ShareFileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(String fragmentTitle, String keyWord){
        searchWord = keyWord;
        return newInstance(fragmentTitle);
    }

    public void setUpRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //我要加载的文件列表
        List<FileEntity> mFileList= new ArrayList<>();
        //获取用户对象
        //ServiceHelper serviceHelper = new ServiceHelper();
        //UserInfoEntity mUser = serviceHelper.LocalToUser(recyclerView.getContext());


        //test region
        for(int i = 0; i<5; i++){
            mFileList.add(new FileEntity("123",FileEntity.PDF,"PDF "+i));
            mFileList.add(new FileEntity("123",FileEntity.PPT,"PPT1 "+i));
            mFileList.add(new FileEntity("123",FileEntity.EXCEL,"EXCEL1 "+i));
            mFileList.add(new FileEntity("123",FileEntity.WORD,"WORD1 "+i));
        }

        //end test region


        //根据标题名称来判断要加载的文件列表内容
        switch(fragmentTitle){
            case UPLOAD:
                //mFileList = serviceHelper.getUploadFileEntities(mUser.getUserCode(),mUser.getPwdCode());
                recyclerView.setAdapter(new SingleUploadFileAdapter(getActivity(),mFileList));
                break;
            case RECEIVE:
                //mFileList = serviceHelper.getReceiveFileEntities(mUser.getUserCode(),mUser.getPwdCode());
                recyclerView.setAdapter(new SingleReceiveFileAdapter(getActivity(),mFileList));
                break;
            case LOADING:
                //mFileList = serviceHelper.getLoadingFileEntities(mUser.getUserCode(),mUser.getPwdCode());
                recyclerView.setAdapter(new SingleLoadingFileAdapter(getActivity(),mFileList));
                break;
                //展示文件检索结果的时候
            case "search":
                //mFileList = serviceHelper.getSearchFileEntities(mUser.getUserCode(),mUser.getPwdCode(),searchWord);
                recyclerView.setAdapter(new SingleSearchFileAdapter(getActivity(),mFileList));

        }



    }

    //定义用于接收已上传文件信息的adapter
    class SingleUploadFileAdapter extends RecyclerView.Adapter<SingleUploadFileAdapter.ViewHolder>{

        protected Context mContext;
        protected List<FileEntity> mFileList;

        public SingleUploadFileAdapter(Context context, List<FileEntity> files){
            mContext = context;
            mFileList = files;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //创建viewHolder，把layout实例化成为一个view
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_base_upload_file, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            //分别把每个数据传进对应的位置中去
            holder.mFileName.setText(mFileList.get(position).getFileName());
            holder.mFileSender.setText(mFileList.get(position).getUploadUserCode());
            this.onBindImageLeft(holder,position);
            this.onBindImageRight(holder,position);

        }

        public void onBindImageLeft(ViewHolder holder, int position){
            switch (mFileList.get(position).getFileType()){
                case FileEntity.WORD:
                    holder.mFileFace.setImageDrawable(mContext.getDrawable(R.drawable.doc));
                    break;
                case FileEntity.EXCEL:
                    holder.mFileFace.setImageDrawable(mContext.getDrawable(R.drawable.xls));
                    break;
                case FileEntity.PPT:
                    holder.mFileFace.setImageDrawable(mContext.getDrawable(R.drawable.ppt));
                    break;
                case FileEntity.PDF:
                    holder.mFileFace.setImageDrawable(mContext.getDrawable(R.drawable.pdf));
                    break;

            }
        }

        public void onBindImageRight(ViewHolder holder, int position){
            holder.mFileState.setImageDrawable(mContext.getDrawable(R.drawable.ic_send_blue));
        }

        @Override
        public int getItemCount() {

            return mFileList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView mFileFace;

            TextView mFileName;

            TextView mFileSender;

            //表示文件是否被下载
            ImageView mFileState;

            public ViewHolder(View view){
                super(view);
                mFileFace = view.findViewById(R.id.file_face);

                mFileName = view.findViewById(R.id.file_name);

                mFileSender = view.findViewById(R.id.file_sender);

                //表示文件是否被下载
                mFileState = view.findViewById(R.id.download_state);

            }


        }
    }

    //定义用于接收已接收文件信息的adapter
    class SingleReceiveFileAdapter extends SingleUploadFileAdapter{

        public SingleReceiveFileAdapter(Context context, List<FileEntity> files) {
            super(context, files);
        }

        @Override
        public void onBindImageRight(ViewHolder holder, int position){
            holder.mFileState.setImageDrawable(mFileList.get(position).isIfLocal()? mContext.getDrawable(R.drawable.ic_done_green) : mContext.getDrawable(R.drawable.ic_download));
        }
    }

    //定义用于接收正在传输文件的adapter
    class SingleLoadingFileAdapter extends SingleUploadFileAdapter{


        public SingleLoadingFileAdapter(Context context, List<FileEntity> files) {
            super(context, files);
        }

        @Override
        public void onBindImageRight(ViewHolder holder, int position){
            //动态展示是否正在下载
            holder.mFileState.setImageDrawable(mFileList.get(position).isIfLocal()? mContext.getDrawable(R.drawable.ic_done_green) : mContext.getDrawable(R.drawable.ic_download));
        }

    }

    //定义用于展示检索文件结果的adapter
    class SingleSearchFileAdapter extends SingleUploadFileAdapter{
        public SingleSearchFileAdapter(Context context, List<FileEntity> files){
            super(context,files);
        }

        //检索结果左边如何展示
        @Override
        public void onBindImageRight(ViewHolder holder, int position){
            holder.mFileState.setImageDrawable(mFileList.get(position).isIfLocal()? mContext.getDrawable(R.drawable.ic_done_green) : mContext.getDrawable(R.drawable.ic_download));
        }
    }
}
