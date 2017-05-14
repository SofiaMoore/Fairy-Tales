package ru.sofiamoore.fairytales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class AuthorActivity extends AppCompatActivity implements ListView.OnItemClickListener {
	
	public static final String EXTRA_AUTHOR = "author";

	private ListView list;
	private ArrayAdapter<String> adapter;
	private String author;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_author);
		//устанавливает заголовок(SetTitle)
		setTitle(author = getIntent().getStringExtra(EXTRA_AUTHOR));
		list = (ListView)findViewById(android.R.id.list);
		//создает
		list.setAdapter(adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1));
		list.setOnItemClickListener(this);
	}
    // помещает в листвью
	@Override
	protected void onResume() {
		adapter.clear();
		//Создаем фильтр файлов, который фильтрует только htm
		FilenameFilter filter = new FilenameFilter() {

			@Override
			public boolean accept(File p1, String p2) {
				return p2.contains(".") && p2.substring(p2.lastIndexOf(".")).equals(".htm");
			}
		};
		//перебирает все элементы массива
		//getExternalFilesDir(author) - директория, у нее вызывает лист с параметром фильтра,
		//который отсеивает все файлы кроме htm и добавляем их в адаптер
		for (String string: getExternalFilesDir(author).list(filter)) {
			//убираем .htm
			adapter.add(string.substring(0, string.lastIndexOf(".")));
		}
		
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
		Intent intent = new Intent(this, TaleActivity.class);
		intent.putExtra(EXTRA_AUTHOR, author);
		intent.putExtra(TaleActivity.EXTRA_TALE, adapter.getItem(p3));
		startActivity(intent);
	}
}
