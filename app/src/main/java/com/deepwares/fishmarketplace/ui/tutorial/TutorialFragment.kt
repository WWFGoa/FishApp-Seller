package com.deepwares.fishmarketplace.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.deepwares.fishmarketplace.BaseDialogFragment
import com.deepwares.fishmarketplace.R
import com.deepwares.fishmarketplace.ui.tutorial.TutorialAdapter
import kotlinx.android.synthetic.main.fragment_tutorial.*

class TutorialFragment : BaseDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // val webView = view.findViewById<WebView>(R.id.webview)
        //webView.setWebChromeClient(WebChromeClient());
        //webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("https://xd.adobe.com/view/8310cba4-4736-4e29-9e13-58797ea78109-d643/?fullscreen")

        // webView.loadUrl("https://xd.adobe.com/view/8310cba4-4736-4e29-9e13-58797ea78109-d643")
        // webView.loadUrl("https://www.google.com")


        view_pager.adapter = TutorialAdapter()
        view_pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }
}