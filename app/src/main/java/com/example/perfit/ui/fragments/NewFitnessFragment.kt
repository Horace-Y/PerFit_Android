package com.example.perfit.ui.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perfit.databinding.FragmentNewFitnessBinding
import com.example.perfit.databinding.FragmentProcessingDialogBinding
import com.example.perfit.models.FitnessResult
import com.example.perfit.ui.FitnessActionSelectionActivity
import com.example.perfit.ui.FitnessResultActivity
import com.example.perfit.utils.Constants.Companion.KEY_ID
import com.example.perfit.utils.Constants.Companion.KEY_MODE
import com.example.perfit.utils.Constants.Companion.KEY_VIDEO
import com.example.perfit.utils.Constants.Companion.RECORDING_DURATION_LIMIT
import com.example.perfit.utils.Constants.Companion.URI1
import com.example.perfit.utils.NetworkResult
import com.example.perfit.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedInputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class NewFitnessFragment : Fragment() {

    private var _binding: FragmentNewFitnessBinding? = null
    private val binding get() = _binding!!
    private var _dialogBinding: FragmentProcessingDialogBinding? = null
    private val dialogBinding get() = _dialogBinding!!
    private var selectedAction: String? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var selectLauncher: ActivityResultLauncher<Intent>
    private lateinit var recordLauncher: ActivityResultLauncher<Intent>
    private lateinit var uploadLauncher: ActivityResultLauncher<Intent>
    private lateinit var alertDialog: AlertDialog
    private lateinit var fitnessResult: FitnessResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        // intent activity launchers
        setUpIntentLaunchers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewFitnessBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        _dialogBinding = FragmentProcessingDialogBinding.inflate(inflater, container, false)

        if (selectedAction != null){
            binding.textSelectedAction.text = "Selected: $selectedAction"
        }

        // loading dialog & spinner
        dialogBinding.progressBar.isIndeterminate = false
        alertDialog = AlertDialog.Builder(context).apply {
            setCancelable(false)
            setView(dialogBinding.root)
        }.create()

        // onClick listeners
        binding.buttonModeSelection.setOnClickListener { selectFitnessAction() }
        binding.buttonStartRecording.setOnClickListener { startVideoRecording() }
        binding.buttonUploadVideo.setOnClickListener { selectLocalVideo() }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        _dialogBinding = null
        super.onDestroyView()
    }

    private fun setUpIntentLaunchers(){
        selectLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent: Intent? = result.data
                selectedAction = intent?.getStringExtra("Mode")
                if (selectedAction != null){
                    binding.textSelectedAction.text = "Selected: $selectedAction"
                }
            }
        }
        recordLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent: Intent? = result.data
                uploadVideo(intent?.data)
            }
        }
        uploadLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent: Intent? = result.data
                var duration: Long?
                MediaMetadataRetriever().apply {
                    setDataSource(context, intent?.data)
                    duration = extractMetadata(METADATA_KEY_DURATION)?.toLongOrNull()
                    release()
                }
                if (duration == null){
                    Toast.makeText(context, "Video data cannot be read", Toast.LENGTH_SHORT).show()
                } else if ((duration!!/1000) > RECORDING_DURATION_LIMIT){
                    Toast.makeText(context, "Video exceeds the maximum length allowed", Toast.LENGTH_SHORT).show()
                } else {
                    uploadVideo(intent?.data)
                }
            }
            // TODO: only for demo purpose
            uploadVideo(URI1)
        }
    }

    private fun selectFitnessAction(){
        selectLauncher.launch(Intent(activity, FitnessActionSelectionActivity::class.java))
    }

    private fun startVideoRecording(){
        if (selectedAction == null){
            Toast.makeText(context, "Please select an action first", Toast.LENGTH_SHORT).show()
            return
        }
        // TODO: add custom output path and name
//        val outputPath = File(Environment.getExternalStorageDirectory().toString() + "/" + R.string.app_name).apply { mkdirs() }
//        val outputFile = File.createTempFile("PerFit_recording_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()),
//            ".mp4", outputPath)
//        val outputUri = FileProvider.getUriForFile(context!!,
//            BuildConfig.APPLICATION_ID + ".provider",
//            outputFile)
        recordLauncher.launch(Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_DURATION_LIMIT, RECORDING_DURATION_LIMIT)
//            putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        })
    }

    private fun selectLocalVideo(){
        if (selectedAction == null){
            Toast.makeText(context, "Please select an action first", Toast.LENGTH_SHORT).show()
            return
        }
        uploadLauncher.launch(Intent(Intent.ACTION_PICK).apply {
            type = "video/*"
        })
    }

    private fun uploadVideo(videoUri: Uri?){
        if (videoUri == null){
            Toast.makeText(context, "Video data cannot be read", Toast.LENGTH_SHORT).show()
            return
        }
        // show processing dialog
        alertDialog.show()

        // convert video file to string using base64 encoding
        var videoData: String
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())

        // TODO: only for demo purpose
        val HCpath = "/Users/haolongyang/PerFit/app/src/main/res/raw/basketball.mp4"
        File(HCpath).also {
            ByteArray(it.length().toInt()).also {
                BufferedInputStream(context?.contentResolver?.openInputStream(videoUri)).apply {
                    read(it, 0, it.size)
                    close()
                }
                videoData = encodeToString(it, DEFAULT)
            }
        }
//        File(videoUri.path!!).also {
//            ByteArray(it.length().toInt()).also {
//                BufferedInputStream(context?.contentResolver?.openInputStream(videoUri)).apply {
//                    read(it, 0, it.size)
//                    close()
//                }
//                videoData = encodeToString(it, DEFAULT)
//            }
//        }
        sendVideoThroughApi(timestamp, videoData)

        // TODO: only for demo purpose
//        object : CountDownTimer(5000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//            }
//            override fun onFinish() {
//                alertDialog.dismiss()
//                findNavController().navigate(R.id.action_newFitnessFragment_to_fitnessResultActivity)
//            }
//        }.start()

        // dismiss processing dialog
        alertDialog.dismiss()

        // navigate to feedback activity
        if (this::fitnessResult.isInitialized){
            val intent = Intent(activity, FitnessResultActivity::class.java).apply {
                putExtra("FitnessResults", fitnessResult)
            }
            startActivity(intent)
        }
    }

    private fun sendVideoThroughApi(timestamp: String, video: String){
        mainViewModel.sendRecording(applyPostQueries(timestamp, selectedAction!!, video))
        mainViewModel.sendRecordingResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    fitnessResult = response.data!!
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }
    }

    private fun applyPostQueries(timestamp: String, mode: String, video: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[KEY_ID] = timestamp
        queries[KEY_MODE] = mode
//        queries[KEY_VIDEO] = video
        queries[KEY_VIDEO] = "abc"

        return queries
    }

}