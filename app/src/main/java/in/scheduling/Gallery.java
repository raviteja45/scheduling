package in.scheduling;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by Ravi on 09-06-2018.
 */

public class Gallery extends AppCompatActivity implements View.OnClickListener {

    private static Button selectImages;
    private static GridView galleryImagesGridView;
    private static ArrayList<String> galleryImageUrls;
    private static GridView_Adapter imagesAdapter;
    TaskHolder taskHolder=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        Intent intent = getIntent();
        taskHolder = (TaskHolder) intent.getExtras().get("Object");
        initViews();
        setListeners();
        fetchGalleryImages();
        setUpGridView();


    }


    private void initViews() {
        selectImages = (Button) findViewById(R.id.selectImagesBtn);
        galleryImagesGridView = (GridView) findViewById(R.id.galleryImagesGridView);

    }
    private void fetchGalleryImages() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        galleryImageUrls = new ArrayList<String>();
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            galleryImageUrls.add(imagecursor.getString(dataColumnIndex));
            System.out.println("Array path" + galleryImageUrls.get(i));
        }


    }
    private void setUpGridView() {
        imagesAdapter = new GridView_Adapter(Gallery.this, galleryImageUrls, true);
        galleryImagesGridView.setAdapter(imagesAdapter);
    }

    //Set Listeners method
    private void setListeners() {
        selectImages.setOnClickListener(this);
    }


    //Show hide select button if images are selected or deselected
    public void showSelectButton() {
        ArrayList<String> selectedItems = imagesAdapter.getCheckedItems();

      //  if (selectedItems.size() > 0) {
            selectImages.setText(selectedItems.size() + " - Images Selected");
            selectImages.setVisibility(View.VISIBLE);
        //} /*else
           // selectImages.setVisibility(View.GONE);*/

        //return selectedItems.get(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectImagesBtn:

                //When button is clicked then fill array with selected images
                ArrayList<String> selectedItems = imagesAdapter.getCheckedItems();
                if(selectedItems!=null&&!selectedItems.isEmpty()){
                    taskHolder.setAttachment(selectedItems);
                }
                else{
                    selectedItems.add("default");
                    taskHolder.setAttachment(selectedItems);
                }
                //Send back result to MainActivity with selected images
                Intent intent = new Intent(view.getContext(),Reason.class);
                intent.putExtra("ImageArray", taskHolder);//Convert Array into string to pass data
                setResult(RESULT_OK, intent);//Set result OK
                startActivity(intent);
                finish();//finish activity
                break;

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,Reason.class);
        intent.putExtra("ImageArray", taskHolder);
        finish();
        startActivity(intent);
    }
}