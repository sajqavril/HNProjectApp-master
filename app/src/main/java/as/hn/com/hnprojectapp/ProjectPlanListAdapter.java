package as.hn.com.hnprojectapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.business.Data.ProjectPlanEntity;
import com.hn.gc.views.CircleProgressView;

import java.util.List;

public class ProjectPlanListAdapter extends RecyclerView.Adapter<ProjectPlanListAdapter.NormalItemHolder> {

    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    private Context context;
    private List<ProjectPlanEntity> mDataList;
    private LayoutInflater mLayoutInflater;
    private int ProgressColor = Color.rgb(0xf8, 0x60, 0x30);

    public ProjectPlanListAdapter(List<ProjectPlanEntity> mDataList, Context mContext) {
        this.context = mContext;
        this.mDataList = mDataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public ProjectPlanListAdapter(List<ProjectPlanEntity> mDataList, Context mContext, int iProgressColor) {
        this.context = mContext;
        this.mDataList = mDataList;
        this.ProgressColor = iProgressColor;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    /**
     * 渲染具体的ViewHolder
     *
     * @param viewGroup ViewHolder的容器
     * @param i         一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    public ProjectPlanListAdapter.NormalItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new NormalItemHolder(mLayoutInflater.inflate(R.layout.fragment_base_swipe_list, viewGroup, false));
    }

    /**
     * 绑定ViewHolder的数据。
     *
     * @param viewHolder
     * @param i          数据源list的下标
     */
    @Override
    public void onBindViewHolder(ProjectPlanListAdapter.NormalItemHolder viewHolder, int i) {
        final int j = i;
        ProjectPlanEntity entity = mDataList.get(i);

        if (null == entity)
            return;

        NormalItemHolder holder = (NormalItemHolder) viewHolder;
        bindNormalItem(entity, holder.planTitle, holder.dateText, holder.mCircleBar, holder.mPercentageBar);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProjectPlanEntity entity = mDataList.get(j);
                Intent intent = new Intent(context, PlanVIewActivity.class);
                intent.putExtra("plan", entity);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 决定元素的布局使用哪种类型
     *
     * @param position 数据源List的下标
     * @return 一个int型标志，传递给onCreateViewHolder的第二个参数
     */
    @Override
    public int getItemViewType(int position) {
        //第一个要显示时间
//        if (position == 0)
//            return NORMAL_ITEM;

        return NORMAL_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return mDataList.get(position).getPlanId();
    }

    void bindNormalItem(ProjectPlanEntity entity, TextView planTitle, TextView planDate, CircleProgressView mCircleBar, CircleProgressView mPercentageBar) {

        planDate.setText(entity.GetPublishDate());
        planTitle.setText(entity.getPlanTitle());

        mCircleBar.setProgress(entity.getTimeUsePercentage());
        mCircleBar.setmTxtHint1("使用了");
        mCircleBar.setmTxtHint2("的时间");

        mPercentageBar.setProgress(entity.getEndPercentage());
        mPercentageBar.setmTxtHint1("已完成");
        mPercentageBar.setmTxtHint2("工作量");
    }


    void showNewsDetail(int pos) {
        ProjectPlanEntity entity = mDataList.get(pos);
//        PlanViewActivity.actionStart(mContext, entity.getNewsID(), entity.getRecommendAmount(), entity.getCommentAmount());
    }

    /**
     * 新闻标题
     */
    public class NormalItemHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView planTitle;
        android.support.v7.widget.CardView cardView;
        ImageView newsIcon;
        CircleProgressView mCircleBar;
        CircleProgressView mPercentageBar;
        Button btnAdd;
        Button btnView;

        public NormalItemHolder(final View itemView) {
            super(itemView);
            cardView = (android.support.v7.widget.CardView) itemView.findViewById(R.id.card_view);
            dateText = (TextView) itemView.findViewById(R.id.base_swipe_item_Date);
            planTitle = (TextView) itemView.findViewById(R.id.base_swipe_item_title);
            mCircleBar = (CircleProgressView) itemView.findViewById(R.id.circleProgressbar);
            mPercentageBar = (CircleProgressView) itemView.findViewById(R.id.endPercentage);
//            btnAdd =(Button)itemView.findViewById(R.id.btn_add_percentage);
//            btnView = (Button)itemView.findViewById(R.id.btn_view_percentage);
//            newsIcon = (ImageView) itemView.findViewById(R.id.base_swipe_item_icon);
//            itemView.findViewById(R.id.base_swipe_item_container).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    RecyclerView recyclerView =(RecyclerView) itemView.findViewById(R.id.recyclerView);
//                    int position = recyclerView.indexOfChild(v);
////                    getPosition()
//                    showNewsDetail(position);
//                }
//            });

//            dateText.setBackgroundColor(Color.argb(20, 0, 0, 0));
        }
    }


}
