package ru.sofiamoore.fairytales;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SplashActivity extends AppCompatActivity {

	public static final String PREF_LAST = "last";
	
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//создаем объект штуки, которая позволяет редактировать настройки приложения
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		new DownloadTask().execute();
	}
	
	private class DownloadTask extends AsyncTask {

		@Override
		protected Object doInBackground(Object... p1) {
			File outDir = getExternalFilesDir(null);
			// создаем файл, в который качается, распаковывается, а потом удаляется
			File tempFile = new File(getExternalCacheDir(), "temp.zip");
			
			int i = 0;
			try {
				//скачивает все архивы с 0.zip и т.д.
				for(i = prefs.getInt(PREF_LAST, -1) + 1;; i++) {
					tempFile.createNewFile();

					InputStream input = new URL("https://raw.githubusercontent.com/SofiaMoore/Fairy-Tales/master/tales/" + i + ".zip").openStream();
					OutputStream output = new FileOutputStream(tempFile);

					int length;
					byte[] buffer = new byte[1024];
					while((length = input.read(buffer)) > 0) output.write(buffer, 0, length);

					output.close();
					input.close();
					//распаковка архива
					ZipInputStream stream = new ZipInputStream(new FileInputStream(tempFile));
					ZipEntry entry;

					while((entry = stream.getNextEntry()) != null) {
						File outFile = new File(outDir, entry.getName());
						if(entry.isDirectory()) {
							outFile.mkdir();
						} else {
							outFile.createNewFile();
							output = new FileOutputStream(outFile);
							while((length = stream.read(buffer)) > 0)
								output.write(buffer, 0, length);
								
							output.close();
						}
					}
						
					stream.close();
				}
				//записываем значения послднего архива, для того, чтобы не скачивать одни и те же архивы
			} catch(FileNotFoundException e) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(PREF_LAST, i - 1);
				editor.apply();
				e.printStackTrace();
				return null;
			} catch(IOException e) {
				e.printStackTrace();
				tempFile.delete();
				return e;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			if(result != null) Toast.makeText(SplashActivity.this, result.toString(), Toast.LENGTH_LONG).show();
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
			super.onPostExecute(result);
		}
	}
}
