package com.student.Compass_Abroad.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.modal.staffProfile.StaffProfileModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.razorpay.PaymentResultListener
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.Utils.SocketManager
import com.student.Compass_Abroad.fragments.NotificationFragment
import com.student.Compass_Abroad.fragments.PaymentDetailFragment
import com.student.Compass_Abroad.fragments.home.ApplicationActiveFragment
import com.student.Compass_Abroad.modal.logoutUser.Logout
import com.student.Compass_Abroad.notifications.NotificationPermissionHelper
import com.student.bt_global.Utils.NeTWorkChange
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.Objects

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), PaymentResultListener {
    var binding: ActivityMainBinding? = null
    var navController: NavController? = null
    var neTWorkChange: NeTWorkChange = NeTWorkChange(this)
    private lateinit var notificationPermissionHelper: NotificationPermissionHelper

    private val requestStoragePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {


            } else {


            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        navController = findNavController(this, R.id.main_nav)
        bottomNav = findViewById(R.id.bottom_navigation)



        drawer = findViewById(R.id.drawerLayout)

        activity = WeakReference(this)


        SocketManager.initSocket(this)
        SocketManager.on("accessRevoked", accessRevokedListener)

        SocketManager.on(Socket.EVENT_CONNECT) {

            Log.d("SocketEvent", "Socket connected")
        }
        SocketManager.on(Socket.EVENT_DISCONNECT) {

            Log.d("SocketEvent", "Socket disconnected")
        }
        SocketManager.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e("SocketEvent", "Connection error: ${args.joinToString()}")
        }


        notificationPermissionHelper = NotificationPermissionHelper(this, requestPermissionLauncher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionHelper.requestNotificationPermission()

        }


        checkAndRequestStoragePermission()

        val versionCode = BuildConfig.VERSION_NAME.takeIf { it?.isNotEmpty() == true } ?: "N/A"
        binding?.tv66?.text = "App Version  $versionCode"

        onClicks()


        sharedPre?.getString(AppConstants.Device_IDENTIFIER, ""
        )?.let { Log.d("useraccesstoken", it) }

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        setupWithNavController(bottomNav!!, navController!!)
        setSupportActionBar(binding!!.toolbarDa)

        binding?.tvMyApplicationsNav?.setOnClickListener {
            binding!!.drawerLayout.close()
            val navController = findNavController(R.id.main_nav)
            navController.navigate(R.id.applicationActiveFragment)
        }

        bottomNav!!.setupWithNavController(navController!!)

        bottomNav!!.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    ApplicationActiveFragment.data = null
                    AppConstants.PROGRAM_STATUS = "0"
                    navController?.navigate(R.id.homeFragment)
                    true
                }

                R.id.fragProgramAllProg -> {
                    ApplicationActiveFragment.data = null
                    AppConstants.profileStatus = "0"
                    AppConstants.PROGRAM_STATUS = "1"
                    navController?.navigate(R.id.fragProgramAllProg)
                    true
                }

                R.id.applicationActiveFragment -> {
                    ApplicationActiveFragment.data = null
                    AppConstants.profileStatus = "0"
                    AppConstants.PROGRAM_STATUS = "1"
                    navController?.navigate(R.id.applicationActiveFragment)
                    true
                }

                R.id.fragCommunity -> {
                    ApplicationActiveFragment.data = null
                    AppConstants.profileStatus = "0"
                    AppConstants.PROGRAM_STATUS = "1"
                    navController?.navigate(R.id.fragCommunity)
                    true
                }

                R.id.counsellingFragment2 -> {
                    ApplicationActiveFragment.data = null
                    AppConstants.profileStatus = "0"
                    AppConstants.PROGRAM_STATUS = "1"
                    navController?.navigate(R.id.counsellingFragment2)
                    true
                }

                R.id.uniteliFragment -> {
                    ApplicationActiveFragment.data = null
                    AppConstants.profileStatus = "0"
                    navController?.navigate(R.id.uniteliFragment)
                    true
                }

                else -> false
            }
        }

        navController!!.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragmentProgram ||destination.id == R.id.bookCounsellingFragment ||destination.id == R.id.educationLoanFragment || destination.id == R.id.homeFragment || destination.id == R.id.fragmentAmbassadorGetChat || destination.id == R.id.studyLevelFragment || destination.id == R.id.fragProgramDetailDetails || destination.id == R.id.uniteliFragment || destination.id == R.id.areaOfInterestFragment || destination.id == R.id.paymentDetailFragment || destination.id == R.id.shortListFragment || destination.id == R.id.fragmentNotification || destination.id == R.id.counsellingFragment2 || destination.id == R.id.viewDocFragment || destination.id == R.id.applicationActiveFragment || destination.id == R.id.fragProgramAllProg || destination.id == R.id.fragmentUploadDocuments || destination.id == R.id.fragmentClientEvents || destination.id == R.id.fragmentAgentChat || destination.id == R.id.programFilterFragment || destination.id == R.id.applyProgramFragment || destination.id == R.id.uploadProgramDocFragment || destination.id == R.id.viewAttachmentFragment || destination.id == R.id.compareProgram || destination.id == R.id.programDetails || destination.id == R.id.comparison || destination.id == R.id.shortListedFragment || destination.id == R.id.applicationFilterFragment || destination.id == R.id.leadsFilterFragment || destination.id == R.id.createApplicationFragment || destination.id == R.id.fragAddCommunityPost || destination.id == R.id.applicationFragment || destination.id == R.id.fragmentProgramUploadDocuments || destination.id == R.id.fragmentComments || destination.id == R.id.assignStaffFragment || destination.id == R.id.addFragmentComments || destination.id == R.id.fragmentPostEdit || destination.id == R.id.reactionFragment || destination.id == R.id.addFragmentReply || destination.id == R.id.fragmentCommentEdit || destination.id == R.id.fragmentEditRelpy || destination.id == R.id.applicationDetail || destination.id == R.id.fragmentConversation || destination.id == R.id.fragmentMessage2 || destination.id == R.id.leadFragment || destination.id == R.id.newLeadFragment || destination.id == R.id.fragmentUploadDocuments || destination.id == R.id.webViewFragment || destination.id == R.id.viewReviewFragment || destination.id == R.id.qrFragment || destination.id == R.id.fragmentLeadUploadDocuments || destination.id == R.id.fragmentWorkli || destination.id == R.id.fragmentViewDetail || destination.id == R.id.fragmentVouchers || destination.id == R.id.webViewButton || destination.id == R.id.fragmentWebinars || destination.id == R.id.topDestinationFragment || destination.id == R.id.inDemandCoursesFragment || destination.id == R.id.inDemandInstitution || destination.id == R.id.studentTestimonials || destination.id == R.id.latestUpdateFragment || destination.id == R.id.hybridPlayerActivity
            ) {
                supportActionBar?.hide()
            } else {
                Objects.requireNonNull(supportActionBar)!!.title = destination.label
                supportActionBar?.show()
            }
        }

        val currentFlavor = BuildConfig.FLAVOR.lowercase()

        Log.d("onCreateonCreate", currentFlavor)

        when (currentFlavor) {
            "admisiony", "firmli", "eeriveurope", "compassabroad", "studiepoint", "unitedglobalservices" -> {
                bottomNav!!.menu.findItem(R.id.uniteliFragment).isVisible = true
            }

            else -> {
                bottomNav!!.menu.findItem(R.id.uniteliFragment).isVisible = false
            }
        }


        intent?.let { handleNotificationIntent(it) }
        handleIntent(intent)
    }


    private fun checkForPendingNotifications() {
        val hasPendingNotification =
             sharedPre?.getString("has_pending_notification", "") == "true"

        if (hasPendingNotification) {
            Log.d("NotificationDebug", "Processing pending notification after login")

            // Create intent with saved notification data
            val notificationIntent = Intent().apply {
                putExtra("from_notification", true)
                putExtra(
                    "module_type",
                    sharedPre?.getString("pending_notification_module_type", "")
                )
                putExtra(
                    "module_id",
                    sharedPre?.getString("pending_notification_module_id", "")
                )
                putExtra(
                    "client_number",
                    sharedPre?.getString("pending_notification_client_number", "")
                )
                putExtra(
                    "application_identifier",
                    sharedPre?.getString("pending_notification_app_identifier", "")
                )
                putExtra(
                    "lead_identifier",
                    sharedPre?.getString("pending_notification_lead_identifier", "")
                )
            }

            handleNotificationIntent(notificationIntent)

            // Clear pending notification data
            sharedPre?.apply {
                saveString("has_pending_notification", "")
                saveString("pending_notification_module_type", "")
                saveString("pending_notification_module_id", "")
                saveString("pending_notification_client_number", "")
                saveString("pending_notification_app_identifier", "")
                saveString("pending_notification_lead_identifier", "")
            }
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.let { handleNotificationIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        if (intent.getBooleanExtra("from_notification", false)) {
            val destination = intent.getStringExtra("navigate_to")
            if (destination == "chat") {
                val leadId = intent.getStringExtra("lead_identifier")
                val applicationId = intent.getStringExtra("application_identifier")
                val moduleId = intent.getStringExtra("module_id")
                val clientNumber = intent.getStringExtra("client_number")

                // Navigate to ChatFragment
                openChatFragment(leadId, applicationId, moduleId, clientNumber)
            }
        }

        // Always try to handle via notification intent (if applicable)
        handleNotificationIntent(intent)
    }

    private fun openChatFragment(
        leadId: String?,
        appId: String?,
        moduleId: String?,
        clientNum: String?
    ) {
        val bundle = Bundle().apply {
            putString("leadId", leadId)
            putString("appId", appId)
            putString("moduleId", moduleId)
            putString("clientNum", clientNum)
        }

        val navController = findNavController(R.id.main_nav)
        navController.navigate(R.id.fragmentAgentChat, bundle)
    }

    private fun handleNotificationIntent(intent: Intent) {
        val moduleType = intent.getStringExtra("module_type") ?: return
        val moduleId = intent.getStringExtra("module_id")
        val clientNumber = intent.getStringExtra("client_number")
        val application_identifier = intent.getStringExtra("application_identifier")
        val lead_identifier = intent.getStringExtra("lead_identifier")
        val ambassador_conversation_identifier = intent.getStringExtra("ambassador_conversation_identifier")

        intent.removeExtra("module_type")
        val navController = findNavController(R.id.main_nav)

        when (moduleType) {
            "application_conversation" -> {
                val bundle = Bundle().apply {
                    putString("module_id", moduleId)
                    putString("client_number", clientNumber)
                }
                App.singleton?.applicationIdentifierChat = application_identifier
                App.singleton?.idetity = "applications"
                App.singleton?.chatStatus = "2"
                navController.navigate(R.id.fragmentAgentChat, bundle)
            }

            "lead_conversation" -> {
                val bundle = Bundle().apply {
                    putString("client_number", clientNumber)
                }
                App.singleton?.applicationIdentifierChat = lead_identifier
                App.singleton?.chatStatus = "1"
                App.singleton?.idetity = "leads"
                navController.navigate(R.id.fragmentAgentChat, bundle)
            }

            "ambassador_conversation" -> {
                val bundle = Bundle().apply {
                    putString("client_number", clientNumber)
                    putString("relation_identifier", ambassador_conversation_identifier)
                }
                navController.navigate(R.id.fragmentAmbassadorGetChat, bundle)

                Log.d("ambassador_conversation", ambassador_conversation_identifier.toString())
            }

            "lead_counseling" -> {
                val bundle = Bundle().apply {
                    putString("client_number", clientNumber)
                }
                navController.navigate(R.id.counsellingFragment2, bundle)
            }

            "application" -> {
                val bundle = Bundle().apply {
                    putString("client_number", clientNumber)
                }
                navController.navigate(R.id.applicationActiveFragment, bundle)
            }

        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }

    private fun checkAndRequestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                requestStoragePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                requestStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {

            } else {
                requestStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun onClicks() {

        binding!!.tvHelpSupport.setOnClickListener {
            startActivity(Intent(this@MainActivity, ContactUsActivity::class.java))
        }

        binding!!.tvAssignStaffNav.setOnClickListener {

            findNavController(this@MainActivity, R.id.main_nav).navigate(R.id.assignStaffFragment)
            binding!!.drawerLayout.close()

        }

        binding!!.civProfileImageFd2.setOnClickListener {
            binding!!.drawerLayout.open()
        }

        binding!!.tvMyProfileNav.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }

        binding!!.tvVouchers.setOnClickListener {
            findNavController(this@MainActivity, R.id.main_nav).navigate(R.id.fragmentVouchers)
            binding!!.drawerLayout.close()
        }

        binding!!.tvTransaction.setOnClickListener {
            PaymentDetailFragment.data = ""
            findNavController(this@MainActivity, R.id.main_nav).navigate(R.id.paymentDetailFragment)
            binding!!.drawerLayout.close()
        }

        binding!!.llMyPref.setOnClickListener {
            val intent = Intent(this, SetPreferencesActivity::class.java)
            startActivity(intent)
            binding!!.drawerLayout.close()
        }


        binding!!.fabFpNotificationStu.setOnClickListener {

            startActivity(Intent(this@MainActivity, NotificationFragment::class.java))
        }

        binding!!.tvLogout.setOnClickListener {

            binding!!.drawerLayout.close()

            showLogoutDialog()

        }



    }

    private val accessRevokedListener = Emitter.Listener { args ->
        if (args.isNotEmpty()) {
            val data = args[0] as JSONObject
            val serverDeviceIdentifier = data.optString("device_identifier", null)
            val localDeviceIdentifier = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""
            if (serverDeviceIdentifier == null || serverDeviceIdentifier == localDeviceIdentifier) {
                runOnUiThread {

                    callLogoutApi()
                }
            }
        } else {

            runOnUiThread {

                callLogoutApi()

            }

        }
    }

    private fun callLogoutApi() {
        ViewModalClass().logoutLiveData(
            this,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken,
        ).observe(this) { createCounsellingModel: Logout? ->
            createCounsellingModel?.let { nonNullEditPostModal ->
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                sharedPre?.clearPreferences()
            }
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.logout))
        builder.setMessage(getString(R.string.logout_confirmation))
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            callLogoutApi()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->

            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
    override fun onResume() {
        super.onResume()

        if (!SocketManager.isConnected()) {
            SocketManager.connect()
        }


        hitApiUserDetails()
        checkForPendingNotifications()
    }

    override fun onPause() {
        super.onPause()
        SocketManager.off("accessRevoked")

    }

    private fun hitApiUserDetails() {
        ViewModalClass().getStaffProfileData(
            this, AppConstants.fiClientNumber, sharedPre?.getString(
                AppConstants.Device_IDENTIFIER, ""
            )!!, "Bearer " + CommonUtils.accessToken
        ).observe(this) { staffData: StaffProfileModal? ->
            staffData?.let { nonNullForgetModal ->
                if (staffData.statusCode == 200) {
                    setUserData(staffData)

                    binding?.tvMyDocumentsNav?.setOnClickListener {

                        val intent = Intent(this@MainActivity, MyDocumentActivity::class.java)
                        intent.putExtra("identifier", staffData.data?.studentProfileInfo?.identifier)
                        startActivity(intent)

                        App.singleton?.studentIdentifier =
                            staffData.data?.studentProfileInfo?.identifier

                        sharedPre!!.saveString(
                            AppConstants.USER_IDENTIFIER,
                            staffData.data?.studentProfileInfo?.identifier
                        )
                    }

                    val currentFlavor = BuildConfig.FLAVOR.lowercase()

                    when (currentFlavor) {
                         "firmli", "compassabroad","eeriveurope", "studiepoint" , "unitedglobalservices" -> {
                            val user =
                                staffData.data?.userInfo?.identityInfo?.filter { it.identifier == "RO1743976880086Y25NOHVF85" }

                            if (user?.size == 1) {
                                binding!!.civ1.visibility = View.VISIBLE
                                binding!!.civ2.visibility = View.GONE
                            } else {
                                binding!!.civ1.visibility = View.GONE
                                binding!!.civ2.visibility = View.VISIBLE
                            }
                        }

                        else -> {
                            binding?.civ1?.visibility = View.GONE
                            binding?.civ2?.visibility = View.GONE
                        }
                    }

                    binding!!.civ2.setOnClickListener {
                        val identifier = staffData.data?.studentProfileInfo?.identifier
                        if (!identifier.isNullOrEmpty()) {
                            BecomeaScout(this@MainActivity, identifier)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Identifier not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    binding!!.civ1.setOnClickListener {
                        createReferandShare(this@MainActivity)
                    }

                } else {
                    CommonUtils.toast(this, nonNullForgetModal.message ?: "Failed")
                }
            }
        }
    }

    private fun setUserData(staffData: StaffProfileModal) {
        sharedPre!!.saveString(AppConstants.FIRST_NAME, staffData.data?.userInfo?.first_name)
        sharedPre!!.saveString(AppConstants.LAST_NAME, staffData.data!!.userInfo.last_name)
        sharedPre!!.saveString(AppConstants.DOB, staffData.data!!.userInfo.birthday)

        val firstName = staffData.data?.userInfo?.first_name ?: ""
        val lastName = staffData.data?.userInfo?.last_name ?: ""
        binding?.tname?.text = "$firstName $lastName"

        binding!!.name.text = "Hi, " + "$firstName $lastName"

        binding?.tvRole?.text =
            "Student ID: "+staffData.data.studentProfileInfo?.student_id.toString() ?: "----"


        val profilePictureUrl = staffData.data?.userInfo?.profile_picture_url
        if (!profilePictureUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(profilePictureUrl)
                .into(binding!!.civNavDrawer)
        } else {
            binding?.civNavDrawer?.setImageResource(R.drawable.test_image)
        }

        if (!profilePictureUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(profilePictureUrl)
                .into(binding!!.civProfileImageFd2)
        } else {
            binding?.civProfileImageFd2?.setImageResource(R.drawable.test_image)
        }

        sharedPre!!.saveModel(AppConstants.USER_IMAGE, profilePictureUrl)
    }

    companion object {
        var bottomNav: BottomNavigationView? = null
        var drawer: DrawerLayout? = null
        var activity: WeakReference<ComponentActivity>? = null
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Success! Payment ID: ${p0.orEmpty()}", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed!", Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(neTWorkChange, intentFilter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(neTWorkChange)
        super.onStop()
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPrefs.getLang(newBase ?: return) ?: "en"
        val context = App.updateBaseContextLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    private fun createReferandShare(activity1: Activity) {
        val deviceIdentifier = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty()
        val token = "Bearer ${CommonUtils.accessToken}"
        ViewModalClass().postReferLinkLiveData(
            activity1,
            AppConstants.fiClientNumber,
            deviceIdentifier,
            token,
            "user"
        ).observe(this@MainActivity) { response ->
            if (response == null) {
                Toast.makeText(activity1, "Error: Response is null", Toast.LENGTH_SHORT).show()
                Log.e("SaveReviewResponse", "Response is null")
                return@observe
            }
            Log.d(
                "SaveReviewResponse",
                "Status Code: ${response.statusCode}, Message: ${response.message}"
            )

            if (response.statusCode == 201) {
                val shortUrl = response.data?.shortUrl

                if (!shortUrl.isNullOrEmpty()) {
                    Log.e("ReferralLink", shortUrl)
                    shareReferralLink(activity1, shortUrl)
                } else {
                    Toast.makeText(
                        activity1,
                        "Failed to generate referral link",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    activity1,
                    response.message ?: "Failed to submit review",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun shareReferralLink(activity1: Activity, shortUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this awesome app! Use my referral link: $shortUrl"
            )
        }
        activity1.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun BecomeaScout(activity1: Activity, identifier: String) {
        val deviceIdentifier = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty()
        val token = "Bearer ${CommonUtils.accessToken}"
        ViewModalClass().postBecomeaScoutData(
            activity1,
            AppConstants.fiClientNumber,
            deviceIdentifier,
            token,
            identifier
        ).observe(this@MainActivity) { response ->
            if (response == null) {
                Toast.makeText(activity1, "Error: Response is null", Toast.LENGTH_SHORT).show()
                Log.e("SaveReviewResponse", "Response is null")
                return@observe
            }

            if (response.statusCode == 200) {
                hitApiUserDetails()
            } else {
                Toast.makeText(
                    activity1,
                    response.message ?: "Failed to submit review",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}


