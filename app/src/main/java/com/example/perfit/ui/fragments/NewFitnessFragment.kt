package com.example.perfit.ui.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.perfit.R
import com.example.perfit.databinding.FragmentNewFitnessBinding
import com.example.perfit.databinding.FragmentProcessingDialogBinding
import com.example.perfit.utils.Constants.Companion.RECORDING_DURATION_LIMIT
import com.example.perfit.viewModels.MainViewModel


class NewFitnessFragment : Fragment() {

    private var _binding: FragmentNewFitnessBinding? = null
    private val binding get() = _binding!!
    private var _dialogBinding: FragmentProcessingDialogBinding? = null
    private val dialogBinding get() = _dialogBinding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recordLauncher: ActivityResultLauncher<Intent>
    private lateinit var uploadLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        // intent activity launchers
        recordLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            val intent: Intent? = result.data
            if (result.resultCode == RESULT_OK) {
                uploadVideo(intent?.data)
            }
            uploadVideo(intent?.data) // TODO: only for testing
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
                    Toast.makeText(context, "Video data cannot be read", Toast.LENGTH_LONG)
                } else if (duration!! / 1000 > RECORDING_DURATION_LIMIT){
                    Toast.makeText(context, "Video length cannot exceed 30 seconds", Toast.LENGTH_LONG)
                } else {
                    uploadVideo(intent?.data)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewFitnessBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        _dialogBinding = FragmentProcessingDialogBinding.inflate(inflater, container, false)

        dialogBinding.progressBar.isIndeterminate = false

        // onClick listeners
        binding.buttonStartRecording.setOnClickListener { startVideoRecording() }
        binding.buttonUploadVideo.setOnClickListener { selectLocalVideo() }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        _dialogBinding = null
        super.onDestroyView()
    }

    private fun startVideoRecording(){
//        val outputPath = RECORDING_OUTPUT_PATH + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".mp4"
        recordLauncher.launch(Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_DURATION_LIMIT, RECORDING_DURATION_LIMIT)
//            putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(outputPath)))
        })
    }

    private fun selectLocalVideo(){
        uploadLauncher.launch(Intent(Intent.ACTION_PICK).apply {
            type = "video/*"
        })
    }

    private fun uploadVideo(videoUri: Uri?){
        if (videoUri == null){
            Toast.makeText(context, "Video data cannot be read", Toast.LENGTH_LONG)
        }
        // show processing dialog
        val alertDialog = AlertDialog.Builder(context).apply {
                setCancelable(false)
                setView(dialogBinding.root)
            }.create().apply { show() }

        val window: Window? = alertDialog.window
        WindowManager.LayoutParams().also {
            it.copyFrom(alertDialog.window?.attributes)
            it.width = LinearLayout.LayoutParams.WRAP_CONTENT
            it.height = LinearLayout.LayoutParams.WRAP_CONTENT
            alertDialog.window?.attributes = it
        }
        window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        // TODO: handle network communication
//        Thread.sleep(10000)

        // dismiss processing dialog
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        alertDialog.dismiss()

        findNavController().navigate(R.id.action_newFitnessFragment_to_fitnessResultActivity)
    }


}