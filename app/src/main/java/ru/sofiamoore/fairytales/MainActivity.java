package ru.sofiamoore.fairytales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.File;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {

	private ListView list;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_main);
		list = (ListView)findViewById(android.R.id.list);
		//создание нового адаптера и помещаем его в листвью
		list.setAdapter(adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1));
		list.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		adapter.clear();
		// получает список папок и делает нужное кол-во строчек , помещает каждую папку в адаптер
		for(File file: getExternalFilesDir(null).listFiles())
			if(file.isDirectory())
				adapter.add(file.getName());
		
		super.onResume();
	}
	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
		Intent intent = new Intent(this, AuthorActivity.class);
		//помещает имя файла в интент
		//берет объект нажатой позиции р3 и помещает этот объект в интент(имя файла)
		intent.putExtra(AuthorActivity.EXTRA_AUTHOR, adapter.getItem(p3));
		startActivity(intent);
	}
}
