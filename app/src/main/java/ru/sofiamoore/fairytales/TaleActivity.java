package ru.sofiamoore.fairytales;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class TaleActivity extends AppCompatActivity {

	public static final String EXTRA_TALE = "tale";
	
	private WebView web;
	private String author, tale;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tale);
		
		author = getIntent().getStringExtra(AuthorActivity.EXTRA_AUTHOR);
		tale = getIntent().getStringExtra(EXTRA_TALE);
		// как браузер webView
		web = (WebView)findViewById(R.id.web);
		web.loadUrl("file://" + getExternalFilesDir(author).getAbsolutePath() + '/' + tale + ".htm");
	}
}
