package com.example.saregama;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;

import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    ListView listViewAnil;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );


        listViewAnil= (ListView) findViewById ( R.id.listViewAnil );
        runtimePermission ();


    }
    public void  runtimePermission() {
        Dexter.withContext ( this ).withPermissions ( READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO ).withListener ( new MultiplePermissionsListener () {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
             displaySong ();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
            permissionToken.continuePermissionRequest ();
            }
        } ).check ();
                    }


                    public ArrayList<File> findSong(File file) {
                        ArrayList<File> arrayList = new ArrayList<> ();
                        File[] files = file.listFiles ();

                        for (File singleFile : files) {
                            if (singleFile.isDirectory () && !singleFile.isHidden ()) {
                                arrayList.addAll ( findSong ( singleFile ) );
                            } else {

                                if (singleFile.getName ().endsWith ( ".mp3" ) || singleFile.getName ().endsWith ( ".wav" )) {

                                    arrayList.add ( singleFile );

                                }
                            }
                        }
                        return arrayList;
                    }

                    public void displaySong() {
                        final ArrayList<File> mySong = findSong ( Environment.getExternalStorageDirectory () );
                        items = new String[mySong.size ()];
                        for (int i = 0; i < mySong.size (); i++) {
                            items[i] = mySong.get ( i ).getName ().toString ().replace ( ".mp3", "" ).replace ( ".wav", "" );


                        }
                        customAdapter customAdapter = new customAdapter ();
                        listViewAnil.setAdapter ( customAdapter );


                        listViewAnil.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String songName = (String) listViewAnil.getItemAtPosition ( position );
                                startActivity ( new Intent ( getApplicationContext (), MusicPlayer.class )
                                        .putExtra ( "song", mySong )
                                        .putExtra ( "songname", songName )
                                        .putExtra ( "pos", position ) );


                            }
                        } );


                    }

                    class customAdapter extends BaseAdapter {

                        @Override
                        public int getCount() {
                            return items.length;
                        }

                        @Override
                        public Object getItem(int i) {
                            return null;
                        }

                        @Override
                        public long getItemId(int i) {
                            return 0;
                        }

                        @Override
                        public View getView(int i, View view, ViewGroup viewGroup) {
                            View view1 = getLayoutInflater ().inflate ( R.layout.item_listanil, null );
                            TextView txtSongNameAnil = view1.findViewById ( R.id.txtSongNameAnil );
                            txtSongNameAnil.setSelected ( true );
                            txtSongNameAnil.setText ( items[i] );
                            return view1;
                        }
                    }
                }
