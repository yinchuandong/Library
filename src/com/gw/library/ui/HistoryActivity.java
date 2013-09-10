package com.gw.library.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gw.library.R;
import com.gw.library.base.BaseHandler;
import com.gw.library.base.BaseMessage;
import com.gw.library.base.BaseTask;
import com.gw.library.base.BaseUi;
import com.gw.library.base.BaseUiAuth;
import com.gw.library.base.C;
import com.gw.library.base.GwListView;
import com.gw.library.base.GwListView.OnRefreshListener;
import com.gw.library.model.History;

public class HistoryActivity extends BaseUiAuth {

	GwListView listView;
	HistoryAdapter historyAdapter;
	LinkedList<HashMap<String, String>> data = new LinkedList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_history);
		pullToRefresh();
		// 设置界面消息处理器
		this.setHandler(new HistoryHandler(this));

		// super.recommend

	}

	/**
	 * 下拉刷新 +
	 */
	public void pullToRefresh() {

		listView = (GwListView) findViewById(R.id.history_list);
		historyAdapter = new HistoryAdapter();
		listView.setAdapter(historyAdapter);
		// 响应下拉事件
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						history_update();
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						historyAdapter.notifyDataSetChanged();
						listView.onRefreshComplete();
					}

				}.execute();
			}
		});
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		history_update();
	}

	// 获取历史列表的更新信息
	public void history_update() {
		HashMap<String, String> hisatoryParams = new HashMap<String, String>();
		hisatoryParams.put("typeID", "0");
		hisatoryParams.put("pageID", "0");
		this.doTaskAsync(C.task.historyList, C.api.historyList, hisatoryParams);
	}

	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		// TODO 获取网络请求之后进行回调处理
		super.onTaskComplete(taskId, message);
		switch (taskId) {
		case C.task.historyList:
			try {
				final ArrayList<History> historyList = (ArrayList<History>) message
						.getDataList("History");
				data.clear();// 清除保留的数据
				for (History history : historyList) {
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("title", history.getTitle());
					item.put("author", history.getAuthor());
					item.put("return_time", history.getReturnTime());
					data.add(item);
				}

				// 保存在数据库里缓存
				// 更新UI
				historyAdapter.notifyDataSetChanged();
				listView.onRefreshComplete();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				toast(e.getMessage());
			}
			break;

		}
	}

	class HistoryAdapter extends BaseAdapter {

		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.tpl_history_item, null);
			TextView title = (TextView) convertView.findViewById(R.id.title);
			TextView author = (TextView) convertView.findViewById(R.id.author);
			TextView return_time = (TextView) convertView
					.findViewById(R.id.return_time);

			title.setText(data.get(position).get("title"));
			author.setText(data.get(position).get("author"));
			return_time.setText(data.get(position).get("return_time"));
			return convertView;
		}

		public long getItemId(int position) {
			return position;
		}

		public Object getItem(int position) {
			return data.get(position);
		}

		public int getCount() {
			return data.size();
		}
	}

	class HistoryHandler extends BaseHandler {

		public HistoryHandler(BaseUi ui) {
			super(ui);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				switch (msg.what) {
				case BaseTask.LOAD_IMAGE:

					break;

				default:
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

}
