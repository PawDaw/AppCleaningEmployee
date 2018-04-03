package com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawdaw.ordering_cleaning_products.controller.managerClasses.WorkplaceActivity;
import com.example.pawdaw.ordering_cleaning_products.model.adapter.PlaceAdapterCustom;
import com.example.pawdaw.ordering_cleaning_products.model.fragments.List_Fragment;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Manager;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;
import com.example.pawdaw.ordering_cleaning_products.controller.MainActivity;
import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.model.service.PermissionService;
import com.example.pawdaw.ordering_cleaning_products.model.service.alertDialog.AlertDialogCompleteListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.OnCompleteTaskListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.Service;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pawdaw on 14/11/16.
 */
public class WorkplaceActivityEmployee extends AppCompatActivity implements OnCompleteTaskListener,NavigationView.OnNavigationItemSelectedListener,AlertDialogCompleteListener {

    private ListView listView;
    private ArrayList<Place> userPlacesTemp = new ArrayList<Place>();
    private PlaceAdapterCustom placeAdapterCustom;
    private String employeeName, phoneNumber;
    private User userObject;
    private Place place;
    private boolean portraitMode = false;
    private TextView userName,emailText;
    private ImageView image_circle;


    private List_Fragment listFra;

    // ---  Firebase  authentication -----
    private FirebaseAuth authentication;
    private FirebaseAuth.AuthStateListener authListener;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawer_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PermissionService.getInstance().askGeoLocationPermission(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Send Message + Picture", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                // send EMAIL with password
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"root13333@gmail.com"} );
                //intent.putExtra(Intent.EXTRA_SUBJECT,getString(user.getDisplayName()));
                //intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_email) + password + getString(R.string.message_2_email));

                intent.setType("message/rfc822");

                startActivity(intent.createChooser(intent,"Select email: "));

                //finish();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add another Layout
        View view = navigationView.getHeaderView(0);
        image_circle = (ImageView) view.findViewById(R.id.image_circle);
        userName = (TextView) view.findViewById(R.id.userNameDrawer);
        emailText = (TextView) view.findViewById(R.id.emailTextDrawer);

        ////add another Layout
        //image_circle = (ImageView) findViewById(R.id.image_circle);
        //userName = (TextView) findViewById(R.id.userNameDrawer);
        //emailText = (TextView) findViewById(R.id.emailTextDrawer);


        //   ---- EXECUTE ASYNCTASK, PROGRESS BAR ----
        Service.getInstance().getPlacesAsyncTaskCallBack(this,this);
        Service.getInstance().getDataFromFirebaseEmployeeCallBack(this,this);

        // load data from Firebse and prepare for next activity CheckInActivity
        Service.getInstance().getCheckInLogCurrentDate();


        //Get Firebase authentication instance
        authentication = FirebaseAuth.getInstance();

        // Authentication Listener - detecting user's session
        // If session is off (user is null)
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {

                    // User authentication state is changed - user is null, Launch MainActivity (MainActivity view)
                    Intent logOff = new Intent(WorkplaceActivityEmployee.this, MainActivity.class);
                    logOff.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logOff);
                }
            }


        };


        employeeName = user.getDisplayName();

        // SET the profile image
        setPictureToImageCircle();

        //  lable to top BAR
        setTitle(employeeName);


        //   ---- EXECUTE ASYNCTASK, PROGRESS BAR ----
        //Service.getInstance().getPlaceFromEmployeeAsyncTask(employeeName,this,this);


        if(WorkplaceActivityEmployee.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            // Portrait Mode

            portraitMode = true;

            listView = (ListView) findViewById(R.id.workPlaceList);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                placeAdapterCustom = new PlaceAdapterCustom(this, R.layout.row, Service.getInstance().getPlaceForEmployee(employeeName));
                listView.setAdapter(placeAdapterCustom);
                placeAdapterCustom.notifyDataSetChanged();



            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String placeName = userPlacesTemp.get(position).getName();

                    if(PermissionService.getInstance().askGeoLocationPermission(WorkplaceActivityEmployee.this)){

                        // Extras to send data to CheckInActivity
                        Intent toDetails = new Intent(WorkplaceActivityEmployee.this, CheckInActivity.class);
                        toDetails.putExtra("employeeName", employeeName);
                        toDetails.putExtra("employeePlaceName", placeName);
                        toDetails.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(toDetails);

                        Service.getInstance().getOrderedProductsCurrentDate_Place(placeName);

                        // ContextMenu functionality
                        registerForContextMenu(listView);
                    }


                }
            });

        } else {
            // Landscape Mode

            portraitMode = false;

            listFra = new List_Fragment();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.side_layout, listFra);
            ft.commit();

        }


    }



    // --- On Click Image, position captured from Adapter ( workPlaceAdapter)

    public void key_On_Click_image(View view) {

        int itemPosition = (int) view.getTag();

        place = userPlacesTemp.get(itemPosition);

        // Put values to extras
        Intent passwordDetail = new Intent(WorkplaceActivityEmployee.this, PasswordDetailWorkPlaceActivity.class);
        passwordDetail.putExtra("place",place);
        startActivity(passwordDetail);

    }

    public void house_On_Click_image(View view) {

        int itemPosition = (int) view.getTag();

        place = userPlacesTemp.get(itemPosition);

        // Put values to extras
        Intent mapPlace = new Intent(WorkplaceActivityEmployee.this, MapPlaceDetailActivity.class);
        mapPlace.putExtra("place", place);
        startActivity(mapPlace);

    }
    //
    //@Override
    //public void OnChangeTaskListener() {
    //
    //    Log.e("info WorkplaceActivi :", " firebase Auto update Workplace ");
    //    if(portraitMode){
    //
    //        userPlacesTemp = Service.getInstance().getPlaceForEmployee(employeeName);
    //        placeAdapterCustom = new PlaceAdapterCustom(WorkplaceActivityEmployee.this, R.layout.row, Service.getInstance().getPlaceForEmployee(employeeName));
    //        listView.setAdapter(placeAdapterCustom);
    //        placeAdapterCustom.notifyDataSetChanged();
    //
    //    }
    //
    //    // update the profile image
    //    setPictureToImageCircle();
    //}

    @Override
    public void onTaskCompleteAsyncTask() {


        Log.e("info WorkplaceActivi :", " firebase Auto update employee ");
        if(portraitMode){

            userPlacesTemp = Service.getInstance().getPlaceForEmployee(employeeName);
            placeAdapterCustom = new PlaceAdapterCustom(WorkplaceActivityEmployee.this, R.layout.row, Service.getInstance().getPlaceForEmployee(employeeName));
            listView.setAdapter(placeAdapterCustom);
            placeAdapterCustom.notifyDataSetChanged();

        }

        // update the profile image
        setPictureToImageCircle();


    }

    public void setPictureToImageCircle(){

        for(User u:Storage.getInstance().getEmployees()){
            if(u.getUsername().equals(employeeName)){
                userObject = u;
                userName.setText(u.getUsername());
                emailText.setText(u.getEmail());

                if(u.getImage() != null){
                    Picasso.with(this).load(u.getImage().getUrl()).into(image_circle);

                }
            }
        }

    }



    // AuthenticationListener starts user's session
    @Override
    public void onStart() {
        super.onStart();
        authentication.addAuthStateListener(authListener);
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {

            getFragmentManager().popBackStack();
        } else {
            // Block "Back" button - employee must log off with button "LOG OFF" to finish the session
            Toast.makeText(this, "Please use LOGOFF button", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_overview) {

            // Put values to extras
            Intent detailPlace = new Intent(WorkplaceActivityEmployee.this, DetailEmployeeActivity.class);
            detailPlace.putExtra("user", userObject);
            startActivity(detailPlace);

        } else if (id == R.id.reports) {

            // Put values to extras
            Intent reportList = new Intent(WorkplaceActivityEmployee.this, ReportActivityList.class);
            reportList.putExtra("user", userObject);
            startActivity(reportList);

        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_phone) {

            for(Manager m:Storage.getInstance().getManagers()){
                phoneNumber = m.getMobileNumber();
            }

            Service.getInstance().dialContactPhone(phoneNumber,this);


        } else if (id == R.id.nav_email) {

            //Snackbar.make(view, "Send Message + Picture", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            // send EMAIL with password
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"root13333@gmail.com"} );
            //intent.putExtra(Intent.EXTRA_SUBJECT,getString(user.getDisplayName()));
            //intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_email) + password + getString(R.string.message_2_email));

            intent.setType("message/rfc822");

            startActivity(intent.createChooser(intent,"Select email: "));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onYesButton() {

        authentication.signOut();
    }

    public void onClick_button_LogOut(View view) {

        //AlertDialog
        Service.getInstance().alertDialogLogOut(this,this);

    }


}
