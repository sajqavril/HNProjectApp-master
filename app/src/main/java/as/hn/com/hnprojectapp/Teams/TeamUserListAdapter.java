package as.hn.com.hnprojectapp.Teams;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hn.business.Data.UserInfoEntity;

import java.util.List;

import as.hn.com.hnprojectapp.R;

public class TeamUserListAdapter extends RecyclerView.Adapter<TeamUserListAdapter.NormalItemHolder> {

    private Context context;
    private List<UserInfoEntity> mDataList;
    private LayoutInflater mLayoutInflater;

    public TeamUserListAdapter(List<UserInfoEntity> mDataList, Context mContext) {
        this.context = mContext;
        this.mDataList = mDataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public TeamUserListAdapter.NormalItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new NormalItemHolder(mLayoutInflater.inflate(R.layout.fragment_base_user_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(TeamUserListAdapter.NormalItemHolder viewHolder, int i) {
        final int j = i;
        UserInfoEntity entity = mDataList.get(i);

        if (null == entity)
            return;

        NormalItemHolder holder = (NormalItemHolder) viewHolder;
        bindNormalItem(entity, holder.userName, holder.userCode, holder.userFace);

//        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ProjectPlanEntity entity = mDataList.get(j);
//                Intent intent = new Intent(context, PlanVIewActivity.class);
//                intent.putExtra("plan", entity);
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    @Override
    public long getItemId(int position) {
        return mDataList.get(position).getId();
    }

    void showNewsDetail(int pos) {
        UserInfoEntity entity = mDataList.get(pos);
//        PlanViewActivity.actionStart(mContext, entity.getNewsID(), entity.getRecommendAmount(), entity.getCommentAmount());
    }

    void bindNormalItem(UserInfoEntity entity, TextView uName, TextView uCode, ImageView uFace) {

        uName.setText(entity.getUserName());
        uCode.setText(entity.getUserCode());
//        uFace.setText(entity.getPlanTitle());

    }

    //region 内部数据类
    public class NormalItemHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView userCode;
        android.support.v7.widget.CardView cardView;
        ImageView userFace;
        Button btnAdd;
        Button btnView;

        public NormalItemHolder(final View itemView) {
            super(itemView);
            cardView = (android.support.v7.widget.CardView) itemView.findViewById(R.id.card_view);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userFace = (ImageView) itemView.findViewById(R.id.userFace);
            userCode = (TextView) itemView.findViewById(R.id.userCode);

//            btnAdd =(Button)itemView.findViewById(R.id.btn_add_percentage);
//            btnView = (Button)itemView.findViewById(R.id.btn_view_percentage);
            itemView.findViewById(R.id.base_swipe_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strUCode = userCode.getText().toString();
//                    RecyclerView recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
//                    int position = recyclerView.indexOfChild(v);
//                    getPosition()
//                    showNewsDetail(position);
                }
            });

        }
    }
    //endregion
}
