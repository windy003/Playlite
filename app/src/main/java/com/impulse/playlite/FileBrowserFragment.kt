package com.impulse.playlite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileBrowserFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var currentDir: File = Environment.getExternalStorageDirectory()
    private var doubleBackToExitPressedOnce = false
    private val backHandler = Handler(Looper.getMainLooper())
    private val backRunnable = Runnable { doubleBackToExitPressedOnce = false }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_file_browser, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewFiles)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showFiles(currentDir)
        return view
    }

    private fun showFiles(dir: File) {
        val files = dir.listFiles()?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() })) ?: emptyList()
        recyclerView.adapter = FileListAdapter(files, ::onFileClicked)
    }

    private fun onFileClicked(file: File) {
        if (file.isDirectory) {
            currentDir = file
            showFiles(currentDir)
        } else if (file.isFile && file.canRead() && isVideoFile(file)) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("video_uri", Uri.fromFile(file).toString())
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Cannot open this file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isVideoFile(file: File): Boolean {
        val videoExtensions = listOf("mp4", "mkv", "avi", "mov", "webm")
        return videoExtensions.any { file.name.endsWith(it, ignoreCase = true) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentDir != Environment.getExternalStorageDirectory()) {
                    currentDir = currentDir.parentFile ?: Environment.getExternalStorageDirectory()
                    showFiles(currentDir)
                } else {
                    if (doubleBackToExitPressedOnce) {
                        requireActivity().finish()
                    } else {
                        doubleBackToExitPressedOnce = true
                        Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show()
                        backHandler.postDelayed(backRunnable, 2000)
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backHandler.removeCallbacks(backRunnable)
    }
}
