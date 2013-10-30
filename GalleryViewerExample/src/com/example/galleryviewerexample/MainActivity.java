package com.example.galleryviewerexample;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private static final int SELECT_PICTURE = 1;

	private String selectedImagePath;
	private ImageView img;
	private Button browseButton;
	private Button loadButton;
	private Button browseSetButton;
	private Boolean browseSetButtonClicked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		img = (ImageView) findViewById(R.id.ImageView01);

		browseButton = (Button) findViewById(R.id.browse);
		loadButton = (Button) findViewById(R.id.load);
		browseSetButton = (Button) findViewById(R.id.set);

		browseButton.setOnClickListener(this);
		loadButton.setOnClickListener(this);
		browseSetButton.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.browse:
			browseSetButtonClicked = false;
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"),
					SELECT_PICTURE);
			break;
		case R.id.load:
			try {
				browseSetButtonClicked = false;
				Uri selectedImageUri = Uri.parse(selectedImagePath);
				img.setImageURI(selectedImageUri);
				Toast.makeText(
						getApplicationContext(),
						"Path of photo loaded.",
						Toast.LENGTH_SHORT).show();
			} catch (NullPointerException e) {
				Toast.makeText(getApplicationContext(),
						"Click 'Get Path' first", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.set:
			browseSetButtonClicked = true;
			Intent set = new Intent();
			set.setType("image/*");
			set.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(set, "Select Picture"),
					SELECT_PICTURE);
			break;
		default:
			break;
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				Log.d("TAG", "Image Path : " + selectedImagePath);
				if (browseSetButtonClicked) {
					img.setImageURI(selectedImageUri);
					Toast.makeText(
							getApplicationContext(),
							"You should now see photo you just selected.",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Now select load path to display picture that was just selected.",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		// Cursor cursor = managedQuery(uri, projection, null, null, null);
		Cursor cursor = getApplicationContext().getContentResolver().query(uri,
				projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

}
