package com.example.perfit.ui.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.perfit.R
import com.example.perfit.databinding.FragmentNewFitnessBinding
import com.example.perfit.databinding.FragmentProcessingDialogBinding
import com.example.perfit.ui.FitnessActionSelectionActivity
import com.example.perfit.utils.Constants.Companion.RECORDING_DURATION_LIMIT
import com.example.perfit.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                val mode = intent?.getStringExtra("Mode")
                if (mode != null){
                    selectedAction = "Selected: $mode"
                    binding.textSelectedAction.text = selectedAction
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
                    Toast.makeText(context, "Video data cannot be read", Toast.LENGTH_LONG).show()
                } else if ((duration!!/1000) > RECORDING_DURATION_LIMIT){
                    Toast.makeText(context, "Video length cannot exceed 30 seconds", Toast.LENGTH_LONG).show()
                } else {
                    uploadVideo(intent?.data)
                }
            }
        }
    }

    private fun selectFitnessAction(){
        selectLauncher.launch(Intent(activity, FitnessActionSelectionActivity::class.java))
    }

    private fun startVideoRecording(){
        if (selectedAction == null){
            Toast.makeText(context, "Please select an action first", Toast.LENGTH_LONG).show()
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
            Toast.makeText(context, "Please select an action first", Toast.LENGTH_LONG).show()
            return
        }
        uploadLauncher.launch(Intent(Intent.ACTION_PICK).apply {
            type = "video/*"
        })
    }

    private fun uploadVideo(videoUri: Uri?){
        if (videoUri == null){
            Toast.makeText(context, "Video data cannot be read", Toast.LENGTH_LONG).show()
            return
        }
        // show processing dialog
        alertDialog.show()

        // TODO: handle network communication
//        sendVideoThroughApi(videoUri)

        // TODO: only for demo purpose
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                alertDialog.dismiss()
                findNavController().navigate(R.id.action_newFitnessFragment_to_fitnessResultActivity)
            }
        }.start()

        // dismiss processing dialog
//        alertDialog.dismiss()

        // navigate to feedback activity
//        findNavController().navigate(R.id.action_newFitnessFragment_to_fitnessResultActivity)
    }

//    private fun sendVideoThroughApi(videoUri: Uri){
//        Log.d("RecipesFragment", "request API data called!!!")
//        mainViewModel.sendRecording(recipesViewModel.applyQueries())
//        mainViewModel.recordingResponse.observe(viewLifecycleOwner) { response ->
//            when (response) {
//                is NetworkResult.Success -> {
//                    response.data?.let { mAdapter.setData(it) }
//                }
//                is NetworkResult.Error -> {
////                    loadDataFromCache()
//                    Toast.makeText(
//                        requireContext(),
//                        response.message.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }


}