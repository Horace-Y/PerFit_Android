package com.example.perfit.ui.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
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
import com.example.perfit.models.FitnessActions
import com.example.perfit.models.FitnessFeedback
import com.example.perfit.ui.FitnessActionSelectionActivity
import com.example.perfit.ui.FitnessResultActivity
import com.example.perfit.utils.Constants.Companion.KEY_ID
import com.example.perfit.utils.Constants.Companion.KEY_MODE
import com.example.perfit.utils.Constants.Companion.KEY_VIDEO
import com.example.perfit.utils.Constants.Companion.RECORDING_DURATION_LIMIT
import com.example.perfit.utils.NetworkResult
import com.example.perfit.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class NewFitnessFragment : Fragment() {

    private var _binding: FragmentNewFitnessBinding? = null
    private val binding get() = _binding!!
    private var _dialogBinding: FragmentProcessingDialogBinding? = null
    private val dialogBinding get() = _dialogBinding!!
    private var selectedAction: FitnessActions? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var selectLauncher: ActivityResultLauncher<Intent>
    private lateinit var recordLauncher: ActivityResultLauncher<Intent>
    private lateinit var uploadLauncher: ActivityResultLauncher<Intent>
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        // intent activity launchers
        setUpIntentLaunchers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewFitnessBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        _dialogBinding = FragmentProcessingDialogBinding.inflate(inflater, container, false)

        if (selectedAction != null) {
            binding.textSelectedAction.text = "Selected: ${selectedAction!!.name}"
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

    private fun setUpIntentLaunchers() {
        selectLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent: Intent? = result.data
                selectedAction = intent?.getParcelableExtra<FitnessActions>("Mode")
                if (selectedAction != null) {
                    binding.textSelectedAction.text = "Selected: ${selectedAction!!.name}"
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
                if (duration == null) {
                    Toast.makeText(context, "Video data cannot be read", Toast.LENGTH_SHORT).show()
                } else if ((duration!! / 1000) > RECORDING_DURATION_LIMIT) {
                    Toast.makeText(
                        context,
                        "Video exceeds the maximum length allowed",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    uploadVideo(intent?.data)
                }
            }
        }
    }

    private fun selectFitnessAction() {
        selectLauncher.launch(Intent(activity, FitnessActionSelectionActivity::class.java))
    }

    private fun startVideoRecording() {
        if (selectedAction == null) {
            Toast.makeText(context, "Please select an action first", Toast.LENGTH_SHORT).show()
            return
        }
        recordLauncher.launch(Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_DURATION_LIMIT, RECORDING_DURATION_LIMIT)
        })
    }

    private fun selectLocalVideo() {
        if (selectedAction == null) {
            Toast.makeText(context, "Please select an action first", Toast.LENGTH_SHORT).show()
            return
        }
        uploadLauncher.launch(Intent(Intent.ACTION_PICK).apply {
            type = "video/*"
        })
    }

    private fun uploadVideo(videoUri: Uri?) {
        if (videoUri == null) {
            Toast.makeText(context, "Video data cannot be read", Toast.LENGTH_SHORT).show()
            return
        }

        alertDialog.show()

        // encode video file to base64 string
        val timestamp =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())
        val videoInputStream = context?.contentResolver?.openInputStream(videoUri)
        if (videoInputStream == null) {
            Toast.makeText(context, "Video data cannot be read", Toast.LENGTH_SHORT).show()
            return
        }
        val videoData = encodeToString(videoInputStream.readBytes(), DEFAULT)

        sendVideoThroughApi(timestamp, videoData)
    }

    private fun sendVideoThroughApi(timestamp: String, video: String) {
        val data: HashMap<String, String> = HashMap()
        data[KEY_ID] = timestamp
        data[KEY_MODE] = selectedAction!!.name
        data[KEY_VIDEO] = video

        mainViewModel.sendRecording(data)
        mainViewModel.sendRecordingResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    processServerResponse(response.data!!)
                }
                is NetworkResult.Error -> {
                    alertDialog.dismiss()
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

    private fun processServerResponse(fitnessFeedback: FitnessFeedback){
        // decode base64 string to video file
        val videoBytes = Base64.decode(fitnessFeedback.video, DEFAULT)
        val videoFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "PerFit" + SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".mp4")
        FileOutputStream(videoFile).apply {
            write(videoBytes)
            close()
        }

        mainViewModel.offlineCacheResult(selectedAction!!, fitnessFeedback.score, videoFile.path)

        // launch fitness result activity
        val videoUri = Uri.fromFile(videoFile)
        val intent = Intent(activity, FitnessResultActivity::class.java).apply {
            putExtra("score", fitnessFeedback.score.toString())
            putExtra("video", videoUri.path)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        alertDialog.dismiss()
        startActivity(intent)
    }
}